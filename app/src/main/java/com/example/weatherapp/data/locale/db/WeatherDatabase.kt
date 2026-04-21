package com.example.weatherapp.data.locale.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.locale.dao.WeatherDao
import com.example.weatherapp.data.locale.entity.DailyForecastEntity
import com.example.weatherapp.data.locale.entity.HourlyForecastEntity
import com.example.weatherapp.data.locale.entity.WeatherEntity


@Database(
    entities = [WeatherEntity::class, DailyForecastEntity::class, HourlyForecastEntity::class],
    version = 1,
    exportSchema = false
)

abstract class WeatherDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}