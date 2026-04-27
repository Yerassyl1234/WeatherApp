package com.example.weatherapp.presentation.screens.cities

sealed interface CitiesEffect{
    data class NavigateToWeatherDetails(val cityName: String): CitiesEffect
}