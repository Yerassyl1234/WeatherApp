package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.common.AppResult
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeCurrentWeather(): Flow<Weather?>
    fun observeHourlyForecast(): Flow<List<HourlyForecast>>
    fun observeDailyForecast(): Flow<List<DailyForecast>>
    suspend fun refreshWeather(lat: Double, lon: Double): AppResult<Unit>
}