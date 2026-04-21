package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.common.location.LocationTracker
import com.example.weatherapp.data.locale.dao.WeatherDao
import com.example.weatherapp.data.locale.db.WeatherDatabase
import com.example.weatherapp.data.remote.RetrofitClient
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_db",
        ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDatabase): WeatherDao =
        db.weatherDao()

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi =
        RetrofitClient.weatherApi

    @Provides
    @Singleton
    fun provideLocationTracker(@ApplicationContext context: Context): LocationTracker =
        LocationTracker(context)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        api: WeatherApi,
        dao: WeatherDao,
    ): WeatherRepository = WeatherRepositoryImpl(
        api = api,
        dao = dao,
        apiKey = BuildConfig.WEATHER_API_KEY,
    )
}