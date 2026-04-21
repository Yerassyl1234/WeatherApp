package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.ForecastResponseDto
import com.example.weatherapp.data.remote.dto.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Response<WeatherDto>

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Response<ForecastResponseDto>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}