package com.example.weatherapp.data.remote

import com.example.weatherapp.data.remote.dto.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): WeatherDto

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}