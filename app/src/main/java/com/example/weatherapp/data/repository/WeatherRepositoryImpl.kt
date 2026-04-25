package com.example.weatherapp.data.repository

import com.example.weatherapp.data.common.AppResult
import com.example.weatherapp.data.common.safeApiCall
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.mapper.toDailyEntity
import com.example.weatherapp.data.mapper.toDomain
import com.example.weatherapp.data.mapper.toEntity
import com.example.weatherapp.data.mapper.toHourlyEntity
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val apiKey: String,
) : WeatherRepository {

    override fun observeCurrentWeather(): Flow<Weather?> =
        dao.observeCurrentWeather().map { it?.toDomain() }

    override fun observeHourlyForecast(): Flow<List<HourlyForecast>> =
        dao.observeHourlyForecast().map { list -> list.map { it.toDomain() } }

    override fun observeDailyForecast(): Flow<List<DailyForecast>> =
        dao.observeDailyForecast().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshWeather(lat: Double, lon: Double): AppResult<Unit> {
        val weatherResult = safeApiCall { api.getWeather(lat, lon, apiKey) }
        val cityResult = safeApiCall { api.getCityName(lat, lon, apiKey = apiKey) }

        return when (weatherResult) {
            is AppResult.Error -> weatherResult

            is AppResult.Success -> {
                val response = weatherResult.data

                val cityName = when (cityResult) {
                    is AppResult.Success -> {
                        val geo = cityResult.data.firstOrNull()
                        geo?.localNames?.get("ru") ?: geo?.name ?: ""
                    }
                    is AppResult.Error -> ""
                }

                dao.insertCurrentWeather(response.current.toEntity(cityName))
                dao.updateForecasts(
                    hourly = response.hourly.map { it.toHourlyEntity() },
                    daily = response.daily.map { it.toDailyEntity() },
                    currentTime = System.currentTimeMillis(),
                )

                AppResult.Success(Unit)
            }
        }
    }
}