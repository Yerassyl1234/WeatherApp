package com.example.weatherapp.presentation.screens.cities

import androidx.compose.runtime.Immutable
import com.example.weatherapp.core.ui.util.UiText
import com.example.weatherapp.domain.model.CityWeather

@Immutable
data class CitiesUiState(
    val cities: List<CityWeather> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null,
)
