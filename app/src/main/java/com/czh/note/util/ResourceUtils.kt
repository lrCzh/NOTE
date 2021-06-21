package com.czh.note.util

import com.czh.note.config.AppConfig

fun getString(resId: Int) {
    AppConfig.mContext.resources.getString(resId)
}