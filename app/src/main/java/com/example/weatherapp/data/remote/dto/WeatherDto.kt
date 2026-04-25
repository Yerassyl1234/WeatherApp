package com.example.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OneCallResponseDto(
    val current: CurrentDto,
    val hourly: List<HourlyDto>,
    val daily: List<DailyDto>,
)

@Serializable
data class CurrentDto(
    val temp: Double,
    @SerialName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("wind_gust") val windGust: Double? = null,
    @SerialName("wind_deg") val windDeg: Int,
    val sunrise: Long,
    val sunset: Long,
    val uvi: Double,
    @SerialName("dew_point") val dewPoint: Double,
    val visibility: Int,
    val weather: List<WeatherInfoDto>,
)

@Serializable
data class HourlyDto(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherInfoDto>,
)

@Serializable
data class DailyDto(
    val dt: Long,
    val temp: DailyTempDto,
    val weather: List<WeatherInfoDto>,
    val uvi: Double,
    val summary: String? = null,
)

@Serializable
data class DailyTempDto(
    val min: Double,
    val max: Double,
)

@Serializable
data class WeatherInfoDto(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String,
)