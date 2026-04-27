package com.example.weatherapp.presentation.screens.main

import androidx.lifecycle.viewModelScope
import com.example.weatherapp.BaseViewModel
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.util.UiText
import com.example.weatherapp.data.common.AppException
import com.example.weatherapp.data.common.location.LocationTracker
import com.example.weatherapp.data.common.onError
import com.example.weatherapp.data.common.onSuccess
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.presentation.mapper.toUiText
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : BaseViewModel<MainUiState, MainAction, MainEffect>(
    initialState = MainUiState()
) {

    init {
        observeWeatherData()
    }

    override fun onAction(action: MainAction) {
        when (action) {
            is MainAction.LoadWeather -> loadWeather(action.isRefreshing)
            is MainAction.PermissionDenied -> {updateState {copy(error = AppException.LocationPermissionDenied().toUiText())}}
        }
    }

    private fun observeWeatherData() {
        viewModelScope.launch {
            combine(
                repository.observeCurrentWeather(),
                repository.observeHourlyForecast(),
                repository.observeDailyForecast(),
            ) { weather, hourly, daily ->
                updateState {
                    copy(
                        weather = weather,
                        hourlyForecast = hourly,
                        dailyForecast = daily,
                    )
                }
            }.collect()
        }
    }

    private fun loadWeather(isRefreshing: Boolean = false) {
        updateState {
            copy(
                isLoading = !isRefreshing,
                isRefreshing = isRefreshing,
            )
        }

        viewModelScope.launch {
            val location = locationTracker.getLocation()

            if (location == null) {
                updateState {
                    copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = UiText.StringResource(R.string.core_ui_error_location_disabled)
                    )
                }
                return@launch
            }

            repository.refreshWeather(location.latitude, location.longitude)
                .onSuccess {
                    updateState {
                        copy(isLoading = false, isRefreshing = false, error = null)
                    }
                }
                .onError { exception ->
                    updateState {
                        copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = exception.toUiText(),
                        )
                    }
                }
        }
    }
}