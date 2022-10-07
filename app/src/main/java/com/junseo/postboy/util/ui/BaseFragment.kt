package com.junseo.postboy.util.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.junseo.postboy.util.listener.BaseFragmentListener

abstract class BaseFragment<T: ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> T): Fragment() {

    // viewBinding
    private var _binding: T? = null
    val binding get() = _binding!!

    // Fragment 리스너
    var fragmentListener: BaseFragmentListener? = null

    // backPressCallback
    private lateinit var backPressCallback: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Fragment 리스너 초기화
        if (context is BaseFragmentListener) fragmentListener = context

        // Activity onBackPressedCallback 초기화
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        backPressCallback.let { activity?.onBackPressedDispatcher?.addCallback(this, it) }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)

        initFragment()

        return binding.root
    }

    abstract fun initFragment()

    abstract fun onBackPressed()

    override fun onDetach() {
        super.onDetach()
        backPressCallback.remove()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}