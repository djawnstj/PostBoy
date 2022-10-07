package com.junseo.postboy.service

import android.webkit.WebViewClient
import com.junseo.postboy.repository.BaseRepository
import com.junseo.postboy.util.web.PostBoy
import com.junseo.postboy.util.web.callback.BaseCallback
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.HttpException

class BaseServiceImpl(
    private val postBoyClient: BaseRepository
): BaseService {

    override fun get(uri: String, queryMap: Map<String, String>?, callback: BaseCallback<String>): Disposable =
        postBoyClient.get(uri, queryMap)
            // 이 이후에 실행되는 코드는 메인스레드에서 실행
            .observeOn(AndroidSchedulers.mainThread())
            //////////////////// getObserver()
            .doOnSubscribe { callback.onLoading() }           // 구독할 때 수행될 작업
            .doOnTerminate { callback.endLoading() }        // 스트림이 종료될 때 수행될 작업 (성공과 에러)
            .subscribe({ callback.onSuccess(it) })    // observable 구독
            {
                if(it is HttpException) callback.onFailure(it.message())
                else callback.onError(it)
            }

    override fun post(uri: String, queryMap: Map<String, String>?, callback: BaseCallback<String>): Disposable =
        postBoyClient.post(uri, queryMap)
            // 이 이후에 실행되는 코드는 메인스레드에서 실행
            .observeOn(AndroidSchedulers.mainThread())
            //////////////////// getObserver()
            .doOnSubscribe { callback.onLoading() }           // 구독할 때 수행될 작업
            .doOnTerminate { callback.endLoading() }        // 스트림이 종료될 때 수행될 작업 (성공과 에러)
            .subscribe({ callback.onSuccess(it) })    // observable 구독
            {
                if(it is HttpException) callback.onFailure(it.message())
                else callback.onError(it)
            }

}