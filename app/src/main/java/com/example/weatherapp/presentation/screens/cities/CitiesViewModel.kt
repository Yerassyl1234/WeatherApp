package com.example.weatherapp.presentation.screens.cities

import com.example.weatherapp.BaseViewModel
import com.example.weatherapp.core.ui.util.WeatherBg
import com.example.weatherapp.domain.model.CityWeather

class CitiesViewModel : BaseViewModel<CitiesUiState, CitiesAction, CitiesEffect>(
    initialState = CitiesUiState()
) {

    private val allCities = listOf(
        CityWeather(
            cityName = "Алматы",
            time = "Текущее место",
            temp = 11,
            description = "В основном солнечно",
            tempMin = 5,
            tempMax = 11,
            background = WeatherBg.DAY,
        ),
        CityWeather(
            cityName = "Астана",
            time = "15:58",
            temp = -10,
            description = "В основном облачно",
            tempMin = -22,
            tempMax = -10,
            background = WeatherBg.DAY_RAIN,
        ),
    )

    init {
        updateState { copy(cities = allCities) }
    }

    override fun onAction(action: CitiesAction) {
        when (action) {
            is CitiesAction.CitySelected -> sendEffect(
                CitiesEffect.NavigateToWeatherDetails(action.cityName)
            )
            is CitiesAction.InputSearch -> {
                updateState { copy(query = action.query) }
                filterCities(action.query)
            }
            CitiesAction.Settings -> { }
        }
    }

    private fun filterCities(query: String) {
        val filtered = if (query.isBlank()) {
            allCities
        } else {
            allCities.filter {
                it.cityName.contains(query, ignoreCase = true)
            }
        }
        updateState { copy(cities = filtered) }
    }
}