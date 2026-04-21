package com.example.weatherapp.presentation

import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather

data class WeatherUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val weather: Weather? = null,
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
    val error: String? = null,
)