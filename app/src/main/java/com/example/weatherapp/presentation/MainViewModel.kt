package com.example.weatherapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.location.LocationTracker
import com.example.weatherapp.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val tracker = LocationTracker(app)
    private val api = RetrofitClient.weatherApi

    fun loadWeather() {
        viewModelScope.launch {
            try {
                val location = tracker.getLocation() ?: return@launch
                val weather = api.getWeatherByCoordinates(
                    location.latitude,
                    location.longitude,
                    BuildConfig.WEATHER_API_KEY
                )
                Log.d("Weather", weather.toString())
            } catch (e: Exception) {
                Log.e("Weather", "Error: ${e.message}")
            }
        }
    }
}