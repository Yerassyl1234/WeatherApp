package com.example.weatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.presentation.screens.cities.CitiesScreen
import com.example.weatherapp.presentation.screens.main.MainScreen

@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main,
    ){
        composable<Screen.Main>{
            MainScreen(
                onNavigateToCities = {
                    navController.navigate(Screen.Cities)
                },
            )
        }
        composable<Screen.Cities> {
            CitiesScreen(
                onNavigateToWeather = {
                    navController.popBackStack()
                },
            )
        }
    }
}