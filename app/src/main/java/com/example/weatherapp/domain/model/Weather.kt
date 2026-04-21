package com.example.weatherapp.domain.model

data class Weather(
    val cityName: String,
    val description: String,
    val main: String,
    val iconId: String,
    val temperature: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windGust: Double?,
    val windDirection: Int,
    val sunrise: Long,
    val sunset: Long,
    val tempMin: Double,
    val tempMax: Double,
    val visibility: Int,
    val updatedAt: Long,
)