package com.czh.note.ui.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.czh.note.R
import com.czh.note.databinding.ActivitySettingBinding
import com.czh.note.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar

class SettingActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .transparentStatusBar()
            .init()

        Glide.with(this).load(R.drawable.pic_3).into(binding.ivTopBg)
        setToolbar(binding.toolbar)
    }
}