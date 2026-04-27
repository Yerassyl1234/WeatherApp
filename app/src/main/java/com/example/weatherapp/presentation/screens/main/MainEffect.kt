package com.example.weatherapp.presentation.screens.main

sealed interface MainEffect {
    object NavigateToLocations : MainEffect
    data class Error(val message: String) : MainEffect
    object OpenLocationPicker : MainEffect
}
