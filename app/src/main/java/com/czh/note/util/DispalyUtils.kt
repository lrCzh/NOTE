package com.czh.note.util

import android.util.TypedValue
import com.czh.note.config.AppConfig
import kotlin.math.roundToInt

fun dp2px(dp: Int): Int {
    return dp2px(dp.toFloat())
}

fun dp2px(dp: Float): Int {
    val metrics = AppConfig.mContext.resources.displayMetrics
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    return px.roundToInt()
}

fun sp2px(sp: Int): Int {
    return sp2px(sp.toFloat())
}

fun sp2px(sp: Float): Int {
    val metrics = AppConfig.mContext.resources.displayMetrics
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics)
    return px.roundToInt()
}