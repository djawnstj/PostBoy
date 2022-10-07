package com.junseo.postboy.util.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>(val bindingFactory: (LayoutInflater) -> T): AppCompatActivity() {

    // viewBinding
    val binding: T by lazy { bindingFactory(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initActivity()
    }

    abstract fun initActivity()

}