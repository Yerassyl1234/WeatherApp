package com.example.weatherapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.WeatherDao
import com.example.weatherapp.data.local.entity.DailyForecastEntity
import com.example.weatherapp.data.local.entity.HourlyForecastEntity
import com.example.weatherapp.data.local.entity.WeatherEntity


@Database(
    entities = [WeatherEntity::class, DailyForecastEntity::class, HourlyForecastEntity::class],
    version = 2,
    exportSchema = false
)

abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}