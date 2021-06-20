package com.czh.note

import android.app.Application
import com.czh.note.config.AppConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
    }

}