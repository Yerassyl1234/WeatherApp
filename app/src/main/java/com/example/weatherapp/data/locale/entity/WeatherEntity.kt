package com.example.weatherapp.data.locale.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "current_weather")
data class WeatherEntity(
    @PrimaryKey val id:Int = 1,
    val cityName: String,
    val description: String,
    val main: String,
    val iconId: String,
    val temperature: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windGust: Double?,
    val windDirection: Int,
    val sunrise: Long,
    val sunset: Long,
    val tempMin: Double,
    val tempMax: Double,
    val visibility: Int,
    val updatedAt: Long,
)
