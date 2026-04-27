package com.example.weatherapp.presentation.screens.cities

sealed interface CitiesAction {
    data class CitySelected(val cityName : String) : CitiesAction
    data class InputSearch(val query: String) : CitiesAction
    data object Settings : CitiesAction
}