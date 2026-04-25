package com.example.weatherapp.di

import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.common.location.LocationTracker
import com.example.weatherapp.data.local.db.WeatherDatabase
import com.example.weatherapp.data.remote.RetrofitClient
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.presentation.screens.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            get(),
            WeatherDatabase::class.java,
            "weather_db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<WeatherDatabase>().weatherDao() }
    single { RetrofitClient.weatherApi }
    single { BuildConfig.WEATHER_API_KEY }
    single { LocationTracker(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }

    viewModel { MainViewModel(get(), get()) }
}

