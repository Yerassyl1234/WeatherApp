package com.example.weatherapp.presentation.screens.main

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.util.UiText
import com.example.weatherapp.data.common.location.LocationTracker
import com.example.weatherapp.data.common.onError
import com.example.weatherapp.data.common.onSuccess
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.presentation.mapper.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state.asStateFlow()

    init {
        observeWeatherData()
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.LoadWeather -> loadWeather(action.isRefreshing)
        }
    }

    private fun observeWeatherData() {
        viewModelScope.launch {
            combine(
                repository.observeCurrentWeather(),
                repository.observeHourlyForecast(),
                repository.observeDailyForecast(),
            ) { weather, hourly, daily ->
                _state.update {
                    it.copy(
                        weather = weather,
                        hourlyForecast = hourly,
                        dailyForecast = daily,
                    )
                }
            }.collect()
        }
    }

    private fun loadWeather(isRefreshing: Boolean = false) {
        _state.update {
            it.copy(
                isLoading = !isRefreshing,
                isRefreshing = isRefreshing,
            )
        }

        viewModelScope.launch {
            val location = locationTracker.getLocation()

            if (location == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = UiText.StringResource(R.string.core_ui_error_location_disabled)
                    )
                }
                return@launch
            }

            repository.refreshWeather(location.latitude, location.longitude)
                .onSuccess {
                    _state.update {
                        it.copy(isLoading = false, isRefreshing = false, error = null)
                    }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = exception.toUiText(),
                        )
                    }
                }
        }
    }
}