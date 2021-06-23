package com.czh.note.util

import com.czh.note.config.AppConfig

fun getString(resId: Int): String {
    return AppConfig.mContext.resources.getString(resId)
}

fun getArray(resId: Int): Array<String> {
    return AppConfig.mContext.resources.getStringArray(resId)
}