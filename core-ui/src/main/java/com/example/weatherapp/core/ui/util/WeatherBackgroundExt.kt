package com.example.weatherapp.core.ui.util

import androidx.annotation.DrawableRes
import com.example.weatherapp.core.ui.R

@DrawableRes
fun WeatherBg.toDrawable(): Int = when (this) {
    WeatherBg.MORNING -> R.drawable.bg_morning
    WeatherBg.MORNING_RAIN -> R.drawable.bg_morning_rain
    WeatherBg.DAY -> R.drawable.bg_day
    WeatherBg.DAY_RAIN -> R.drawable.bg_day_rain
    WeatherBg.EVENING -> R.drawable.bg_evening
    WeatherBg.EVENING_RAIN -> R.drawable.bg_evening_rain
    WeatherBg.NIGHT -> R.drawable.bg_night
    WeatherBg.NIGHT_RAIN -> R.drawable.bg_night_rain
}