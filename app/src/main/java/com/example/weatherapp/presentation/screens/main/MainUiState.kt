package com.example.weatherapp.presentation.screens.main

import com.example.weatherapp.core.ui.util.UiText
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather

data class MainUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val weather: Weather? = null,
    val hourlyForecast: List<HourlyForecast> = emptyList(),
    val dailyForecast: List<DailyForecast> = emptyList(),
    val error: UiText? = null,
)