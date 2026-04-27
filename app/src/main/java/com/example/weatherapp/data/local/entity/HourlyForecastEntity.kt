package com.example.weatherapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "hourly_forecast")
data class HourlyForecastEntity(
    @PrimaryKey
    val timeInMillis: Long,
    val main: String,
    val icon: String,
    val temperature: Double,
)
