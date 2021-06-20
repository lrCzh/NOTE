package com.czh.note.config

import android.app.Application
import android.content.Context

object AppConfig {

    private lateinit var mApplication: Application

    val mContext: Context get() = mApplication.applicationContext

    fun init(application: Application) {
        this.mApplication = application
    }
}