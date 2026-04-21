package com.example.weatherapp.data.repository

import com.example.weatherapp.data.common.AppResult
import com.example.weatherapp.data.common.safeApiCall
import com.example.weatherapp.data.locale.dao.WeatherDao
import com.example.weatherapp.data.mapper.toDailyEntities
import com.example.weatherapp.data.mapper.toDomain
import com.example.weatherapp.data.mapper.toEntity
import com.example.weatherapp.data.mapper.toHourlyEntities
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override suspend fun refreshWeather(lat: Double, lon: Double): AppResult<Unit> =
        coroutineScope {
            val weatherDeferred = async { safeApiCall { api.getCurrentWeather(lat, lon, apiKey) } }
            val forecastDeferred = async { safeApiCall { api.getForecast(lat, lon, apiKey) } }

            val weatherResult = weatherDeferred.await()
            val forecastResult = forecastDeferred.await()

            when (weatherResult) {
                is AppResult.Success -> dao.insertCurrentWeather(weatherResult.data.toEntity())
                is AppResult.Error -> return@coroutineScope weatherResult
            }

            when (forecastResult) {
                is AppResult.Success -> {
                    dao.updateForecasts(
                        hourly = forecastResult.data.toHourlyEntities(),
                        daily = forecastResult.data.toDailyEntities(),
                        currentTime = System.currentTimeMillis(),
                    )
                }

                is AppResult.Error -> return@coroutineScope forecastResult
            }

            AppResult.Success(Unit)
        }
}