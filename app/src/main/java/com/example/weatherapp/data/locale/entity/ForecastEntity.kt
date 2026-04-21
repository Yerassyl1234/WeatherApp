package com.example.weatherapp.data.locale.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "daily_forecast")
data class DailyForecastEntity(
    @PrimaryKey
    val dateInMillis: Long,
    val main: String,
    val icon: String,
    val minTemp:Double,
    val maxTemp:Double,
)

@Entity(tableName = "hourly_forecast")
data class HourlyForecastEntity(
    @PrimaryKey
    val timeInMillis: Long,
    val main: String,
    val icon: String,
    val temperature: Double,
)
