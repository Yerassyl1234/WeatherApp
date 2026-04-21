package com.example.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDto(
    @SerialName("name") val cityName: String,
    @SerialName("main") val main: MainDto,
    @SerialName("weather") val weather: List<WeatherInfoDto>,
    @SerialName("wind") val wind: WindDto,
    @SerialName("visibility") val visibility: Int = 0,
    @SerialName("sys") val sys: SysDto? = null
)

@Serializable
data class MainDto(
    @SerialName("temp") val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    @SerialName("humidity") val humidity: Int,
    @SerialName("pressure") val pressure: Int,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
)

@Serializable
data class WindDto(
    @SerialName("speed") val speed: Double,
    @SerialName("deg") val deg: Int = 0,
    @SerialName("gust") val gust: Double? = null
)

@Serializable
data class SysDto(
    @SerialName("sunrise") val sunrise: Long,
    @SerialName("sunset") val sunset: Long,
)

@Serializable
data class WeatherInfoDto(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String,
)