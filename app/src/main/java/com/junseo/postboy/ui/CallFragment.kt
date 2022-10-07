package com.junseo.postboy.ui

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.children
import androidx.room.Room
import com.gregacucnik.EditTextView
import com.junseo.postboy.R
import com.junseo.postboy.databinding.FragmentCallBinding
import com.junseo.postboy.databinding.ItemParamsBinding
import com.junseo.postboy.entity.HttpCall
import com.junseo.postboy.entity.Param
import com.junseo.postboy.repository.BaseRepository
import com.junseo.postboy.repository.local.database.ParamDB
import com.junseo.postboy.service.BaseService
import com.junseo.postboy.service.BaseServiceImpl
import com.junseo.postboy.util.toast.ToastUtil
import com.junseo.postboy.util.ui.BaseFragment
import com.junseo.postboy.util.web.PostBoy
import com.junseo.postboy.util.web.WebConfig
import com.junseo.postboy.util.web.callback.BaseCallback
import java.net.URL

class CallFragment(
    private var callSeq: Long?
): BaseFragment<FragmentCallBinding>(FragmentCallBinding::inflate) {

    companion object { private const val TAG = "CallFragment" }

    private val postBoyClient: BaseRepository get() = PostBoy.create(baseUrl)

    private lateinit var baseService: BaseService

    // 기본 요청 URL
    private var baseUrl = ""
    // 기본 요청 URI
    private var path = ""

    // 파라미터 객체를 담는 배열 변수
    private val params: ArrayList<Param> = ArrayList()
    // 파라미터 맵
    private val paramsMap = HashMap<String, String>()

    // 파라미터 저장 roomDB
    private val db: ParamDB by lazy {
        Room.databaseBuilder(
            requireContext(),
            ParamDB::class.java,
            "param")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    // 파라미터 DB DAO
    private val paramDao by lazy { db.getParamDao() }
    // httpCall DB DAO
    private val callDao by lazy { db.getHttpCallDao() }

    override fun initFragment() {

        initViewEvents()

        setHttpCall()

    }

    /**
     * view 의 이벤트 초기화
     */
    private fun initViewEvents() {

        initCallMethodSpinner()

        // 파라미터 추가 버튼 클릭
        binding.paramsAddButton.setOnClickListener {
            addParamsForm(null, null)
        }

        // url 저장 버튼 클릭
        binding.saveUrlButton.setOnClickListener {
            setUrl()
            // url 저장
            saveHttpCallInRoomDB()
            // 해당 call 의 파라미터를 먼저 삭제
            deleteParamInRoomDB()
            // 파라미터 저장
            saveParamInRoomDB()
        }

        // url 불러오기 버튼 클릭
        binding.loadUrlButton.setOnClickListener {
            setHttpCall()
        }

        // clearParams 버튼 클릭
        binding.clearParamsButton.setOnClickListener {
            deleteAllParamsForm()
        }

        // 에디트 텍스트뷰 입력 리스너(사용안함)
        binding.urlInput.setEditTextViewListener(object: EditTextView.EditTextViewListener {
            override fun onEditTextViewEditModeStart() {
            }

            override fun onEditTextViewEditModeFinish(text: String?) {
                baseService = BaseServiceImpl(postBoyClient)
                val url = URL(text ?: "")

                url.apply {
                    baseUrl = "$protocol://$host:$port"
                    this@CallFragment.path = path
                }

                baseService = BaseServiceImpl(postBoyClient)
            }

        })

    }

    /**
     * RoomDB 에서 조회한 HttpCall 정보로 프래그먼트 세팅
     */
    private fun setHttpCall() {
        val (call, params) = loadHttpCallInRoomDB()

        callSeq = call.httpCallSeq

        baseUrl = call.callUrl
        path = call.callPath

        binding.urlText.setText("$baseUrl$path")

        deleteAllParamsForm()

        this@CallFragment.params.clear()
        this@CallFragment.params.addAll(params)

        params.forEach {
            addParamsForm(it.paramKey, it.paramValue)
        }
    }

    /**
     * RoomDB 에서 HttpCall 정보 불러오기
     */
    private fun loadHttpCallInRoomDB(): Pair<HttpCall, List<Param>> {
        callSeq?.let {
            val callWithParams = callDao.findHttpCallWithParamsById(it)
            val call = callWithParams?.httpCall ?: HttpCall()
            val params = callWithParams?.params ?: listOf()
            return Pair(call, params)
        }
        return Pair(HttpCall(), listOf())
    }

    /**
     * RoomDB에 HttpCall 정보 저장
     */
    private fun saveHttpCallInRoomDB() {
        callSeq = callDao.save(HttpCall(httpCallSeq = this@CallFragment.callSeq, callName = "test", callUrl = baseUrl, callPath = path))
    }

    /**
     * RoomDB에 해당 call 의 파라미터를 먼저 삭제
     */
    private fun deleteParamInRoomDB() {
//        callSeq?.let {
//            paramDao.deleteParamByHttpCallId(it)
//        }
        params.forEach {
            paramDao.deleteParamById(it.phraseSeq ?: -1L)
        }
    }

    /**
     * RoomDB에 파라미터 저장
     */
    private fun saveParamInRoomDB() {
        setParamsMap()
        callSeq?.let { callSeq ->
            paramsMap.forEach {
                val param = Param()
                param.paramKey = it.key
                param.paramValue = it.value
                param.ownerCall = callSeq
                paramDao.save(Param(null, it.key, it.value, callSeq))
            }
        }
    }

    /**
     * 파라미터 form 모두 삭제 함수
     */
    private fun deleteAllParamsForm() {
        binding.parameterScrollView.removeAllViews()
    }

    /**
     * 파라미터 form 추가 함수
     */
    private fun addParamsForm(key: String?, value: String?) {
        val paramsBinding = ItemParamsBinding.inflate(layoutInflater)

        paramsBinding.paramKeyInput.setText(key)
        paramsBinding.paramValueInput.setText(value)

        paramsBinding.deleteParamFormButton.setOnClickListener {
            binding.parameterScrollView.removeView(paramsBinding.root)
        }

        binding.parameterScrollView.addView(paramsBinding.root)
    }

    /**
     * Call Method 스피너 초기화 함수
     */
    private fun initCallMethodSpinner() {
        val items = resources.getStringArray(R.array.call_methods)
        val callMethodAdapter = ArrayAdapter(requireContext(), R.layout.item_call_method, items)

        binding.methodSpinner.adapter = callMethodAdapter

        binding.methodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                binding.callButton.setOnClickListener {
                    call(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun call(position: Int) {

        clearResult()

        if (binding.urlText.text.toString().startsWith("http").not()) {
            ToastUtil.showToast(requireContext(), "URL 입력값이 올바르지 않습니다.")
            return
        }

        setUrl()

        paramsMap.clear()

        setParamsMap()

        baseService = BaseServiceImpl(postBoyClient)

        when (position) {
            0 -> callGetMethod(path, paramsMap)
            1 -> callPostMethod(path, paramsMap)
        }

        printResult("call to... '$baseUrl$path' with ${binding.methodSpinner.selectedItem}")

    }

    private fun clearResult() = binding.result.setText("")

    /**
     * 하단 결과창에 결과 더하기
     */
    private fun printResult(msg: String?) {
        binding.result.append("$msg\n")
    }

    /**
     * 파라미터 form 에 있는 파라미터들 map 변수에 담기
     */
    private fun setParamsMap() {
        binding.parameterScrollView.children.forEach {
            val key = it.findViewById<EditText>(R.id.paramKeyInput).text.toString()
            val value = it.findViewById<EditText>(R.id.paramValueInput).text.toString()

            if (key.isEmpty()) return@forEach

            paramsMap[key] = value
        }
    }

    private fun setUrl() {
        val url = URL(binding.urlText.text.toString() ?: "")

        url.apply {
            baseUrl = "$protocol://$host:$port"
            this@CallFragment.path = path
            WebConfig.setUrl(protocol, host, port)
        }
    }

    private fun callGetMethod(uri: String, queryMap: Map<String, String>?) {
        baseService.get(uri, queryMap, object: BaseCallback<String> {
            override fun onSuccess(data: String) {
                printResult("\ncall onSuccess : \n")
                binding.result.append(data)
            }

            override fun onFailure(description: String) {
                printResult("\ncall onFailure : \n")
                binding.result.append(description)
            }

            override fun onError(throwable: Throwable) {
                printResult("\ncall onError : \n")
                printResult(throwable.message)
            }

            override fun onLoading() {
            }

            override fun endLoading() {
            }

        })
    }

    private fun callPostMethod(uri: String, queryMap: Map<String, String>?) {
        baseService.post(uri, queryMap, object: BaseCallback<String> {
            override fun onSuccess(data: String) {
                printResult("\ncall onSuccess : \n")
                printResult(data)
            }

            override fun onFailure(description: String) {
               printResult("\ncall onFailure : \n")
               printResult(description)
            }

            override fun onError(throwable: Throwable) {
                printResult("\ncall onError : \n")
                printResult(throwable.message)
            }

            override fun onLoading() {
            }

            override fun endLoading() {
            }

        })
    }

    override fun onBackPressed() {
    }

}