package com.example.weatherapp.data.locale.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherapp.data.locale.entity.DailyForecastEntity
import com.example.weatherapp.data.locale.entity.HourlyForecastEntity
import com.example.weatherapp.data.locale.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: WeatherEntity)

    @Query("SELECT * FROM current_weather WHERE id = 1")
    fun observeCurrentWeather(): Flow<WeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(forecasts: List<HourlyForecastEntity>)

    @Query("SELECT * FROM hourly_forecast ORDER BY timeInMillis ASC")
    fun observeHourlyForecast(): Flow<List<HourlyForecastEntity>>

    @Query("DELETE FROM hourly_forecast WHERE timeInMillis < :currentTimeMillis")
    suspend fun deleteOldHourlyForecasts(currentTimeMillis: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(forecasts: List<DailyForecastEntity>)

    @Query("SELECT * FROM daily_forecast ORDER BY dateInMillis ASC")
    fun observeDailyForecast(): Flow<List<DailyForecastEntity>>

    @Query("DELETE FROM daily_forecast WHERE dateInMillis < :currentDateMillis")
    suspend fun deleteOldDailyForecasts(currentDateMillis: Long)

    @Transaction
    suspend fun updateForecasts(
        hourly: List<HourlyForecastEntity>,
        daily: List<DailyForecastEntity>,
        currentTime: Long
    ) {

        insertHourlyForecast(hourly)
        insertDailyForecast(daily)
        deleteOldHourlyForecasts(currentTime)
        val startOfToday = currentTime - (24 * 60 * 60 * 1000)
        deleteOldDailyForecasts(startOfToday)

    }
}