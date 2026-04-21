package com.example.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponseDto(
    @SerialName("list") val list: List<ForecastItemDto>,
)

@Serializable
data class ForecastItemDto(
    @SerialName("dt") val dt: Long,
    @SerialName("main") val main: ForecastMainDto,
    @SerialName("weather") val weather: List<WeatherInfoDto>,
    @SerialName("wind") val wind: WindDto,
)

@Serializable
data class ForecastMainDto(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int,
)