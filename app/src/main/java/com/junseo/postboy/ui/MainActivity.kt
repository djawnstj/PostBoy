package com.junseo.postboy.ui

import com.junseo.postboy.databinding.ActivityMainBinding
import com.junseo.postboy.util.prefs.PrefsUtil
import com.junseo.postboy.util.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>({ActivityMainBinding.inflate(it)}) {

    override fun initActivity() {

        // prefsUtil 싱글톤 초기화
        PrefsUtil.createPrefs(this)

        initView()

    }

    private fun initView() {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, CallFragment(5)).commit()
    }

}