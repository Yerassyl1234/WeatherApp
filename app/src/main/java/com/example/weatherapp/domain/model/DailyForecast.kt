package com.example.weatherapp.domain.model


data class DailyForecast(
    val dateInMillis: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val main: String,
    val icon: String,
    val summary: String? = null,
)