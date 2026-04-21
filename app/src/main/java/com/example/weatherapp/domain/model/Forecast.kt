package com.example.weatherapp.domain.model

data class HourlyForecast(
    val timeInMillis: Long,
    val temperature: Double,
    val main: String,
    val icon: String,
)

data class DailyForecast(
    val dateInMillis: Long,
    val minTemp: Double,
    val maxTemp: Double,
    val main: String,
    val icon: String,
)