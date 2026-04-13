package com.example.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("name") val cityName: String,
    @SerialName("main") val main: MainDto,
    @SerialName("weather") val weather: List<WeatherInfoDto>,
    @SerialName("wind") val wind: WindDto,
)

@Serializable
data class MainDto(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int,
)

@Serializable
data class WeatherInfoDto(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String,
)

@Serializable
data class WindDto(
    @SerialName("speed") val speed: Double,
)