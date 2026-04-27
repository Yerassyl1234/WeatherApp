package com.example.weatherapp.presentation.screens.main

sealed interface MainAction {
    data class LoadWeather(val isRefreshing: Boolean = false) : MainAction
    data object PermissionDenied : MainAction
}