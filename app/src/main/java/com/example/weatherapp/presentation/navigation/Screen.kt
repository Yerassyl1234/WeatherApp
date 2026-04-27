package com.example.weatherapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

    @Serializable
    data object Map:Screen

    @Serializable
    data object Main : Screen

    @Serializable
    data object Cities : Screen

}