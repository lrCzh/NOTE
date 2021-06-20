package com.czh.note.util

import com.czh.note.R
import com.czh.note.config.AppConfig

fun getWeatherIcon(str_weather: String): Int {
    return when (str_weather) {
        AppConfig.mContext.resources.getString(R.string.weather_wumai) -> R.drawable.ic_weather_wumai
        AppConfig.mContext.resources.getString(R.string.weather_duoyun) -> R.drawable.ic_weather_duoyun
        AppConfig.mContext.resources.getString(R.string.weather_yingtian) -> R.drawable.ic_weather_yintian
        AppConfig.mContext.resources.getString(R.string.weather_dalei) -> R.drawable.ic_weather_dalei
        AppConfig.mContext.resources.getString(R.string.weather_xiayu) -> R.drawable.ic_weather_xiayu
        AppConfig.mContext.resources.getString(R.string.weather_xiaxue) -> R.drawable.ic_weather_xiaxue
        AppConfig.mContext.resources.getString(R.string.weather_yujiaxue) -> R.drawable.ic_weather_yujiaxue
        AppConfig.mContext.resources.getString(R.string.weather_taiyangyu) -> R.drawable.ic_weather_taiyangyu
        else -> R.drawable.ic_weather_qingtian
    }
}