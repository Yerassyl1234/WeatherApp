package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.dto.GeocodingDto
import com.example.weatherapp.data.remote.dto.OneCallResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): Response<OneCallResponseDto>

    @GET("geo/1.0/reverse")
    suspend fun getCityName(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String,
    ): Response<List<GeocodingDto>>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
    }
}