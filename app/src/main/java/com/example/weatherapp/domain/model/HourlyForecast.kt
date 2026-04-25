package com.example.weatherapp.domain.model


data class HourlyForecast(
    val timeInMillis: Long,
    val temperature: Double,
    val main: String,
    val icon: String,
)
