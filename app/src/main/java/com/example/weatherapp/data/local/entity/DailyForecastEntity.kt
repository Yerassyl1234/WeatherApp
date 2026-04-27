package com.example.weatherapp.data.local.entity

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
    val summary: String? = null,
)
