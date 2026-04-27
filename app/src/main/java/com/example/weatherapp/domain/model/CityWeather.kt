package com.example.weatherapp.domain.model

import com.example.weatherapp.core.ui.util.WeatherBg

data class CityWeather(
    val cityName:String,
    val time: String,
    val temp:Int,
    val description:String,
    val tempMin:Int,
    val tempMax: Int,
    val background: WeatherBg,
)
