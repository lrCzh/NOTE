package com.czh.note.util

import android.content.Context
import android.os.Vibrator

/**
 * 手机震动相关工具
 */
object VibratorUtils {
    /**
     * 开始震动
     * @param context
     * @param pattern 震动规则
     * @param repeat 循环次数
     */
    fun startVibrator(context: Context, pattern: LongArray, repeat: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
        vibrator.vibrate(pattern, repeat)
    }

    /**
     * 关闭震动
     *
     * @param context
     */
    fun cancelVibrator(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.cancel()
    }

    /**
     * 短震动
     */
    fun shortVibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(10)
    }

    /**
     * 长震动
     */
    fun longVibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(500)
    }
}



