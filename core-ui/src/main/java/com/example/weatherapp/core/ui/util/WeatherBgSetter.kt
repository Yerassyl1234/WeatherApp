package com.example.weatherapp.core.ui.util

fun setWeatherBg(
    hour: Int,
    isRaining: Boolean
): WeatherBg {
    val timeOfDay = when (hour) {
        in 6..11 -> TimeOfDay.MORNING
        in 12..17 -> TimeOfDay.DAY
        in 18..23 -> TimeOfDay.EVENING
        else -> TimeOfDay.NIGHT
    }

    return when (timeOfDay) {
        TimeOfDay.MORNING -> if (isRaining) WeatherBg.MORNING_RAIN else WeatherBg.MORNING
        TimeOfDay.DAY -> if (isRaining) WeatherBg.DAY_RAIN else WeatherBg.DAY
        TimeOfDay.EVENING -> if (isRaining) WeatherBg.EVENING_RAIN else WeatherBg.EVENING
        TimeOfDay.NIGHT -> if (isRaining) WeatherBg.NIGHT_RAIN else WeatherBg.NIGHT
    }
}

private enum class TimeOfDay {
    MORNING,
    DAY,
    EVENING,
    NIGHT
}