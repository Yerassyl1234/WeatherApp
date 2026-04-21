package com.example.weatherapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.common.AppException
import com.example.weatherapp.data.common.AppResult
import com.example.weatherapp.data.common.location.LocationTracker
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val tracker: LocationTracker,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        repository.observeCurrentWeather()
            .onEach { weather -> _uiState.update { it.copy(weather = weather) } }
            .launchIn(viewModelScope)

        repository.observeHourlyForecast()
            .onEach { hourly -> _uiState.update { it.copy(hourlyForecast = hourly) } }
            .launchIn(viewModelScope)

        repository.observeDailyForecast()
            .onEach { daily -> _uiState.update { it.copy(dailyForecast = daily) } }
            .launchIn(viewModelScope)
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val location = tracker.getLocation()
            if (location == null) {
                _uiState.update { it.copy(isLoading = false, error = "Не удалось определить местоположение") }
                return@launch
            }

            val result = repository.refreshWeather(location.latitude, location.longitude)
            when (result) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false) }
                is AppResult.Error -> _uiState.update {
                    it.copy(isLoading = false, error = result.exception.toUiMessage())
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            val location = tracker.getLocation()
            if (location == null) {
                _uiState.update { it.copy(isRefreshing = false, error = "Не удалось определить местоположение") }
                return@launch
            }

            val result = repository.refreshWeather(location.latitude, location.longitude)
            _uiState.update {
                when (result) {
                    is AppResult.Success -> it.copy(isRefreshing = false)
                    is AppResult.Error -> it.copy(isRefreshing = false, error = result.exception.toUiMessage())
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun AppException.toUiMessage(): String = when (this) {
        is AppException.NoInternet -> "Нет подключения к интернету"
        is AppException.Timeout -> "Сервер не отвечает"
        is AppException.ServerError -> "Ошибка сервера"
        is AppException.ClientError -> "Ошибка запроса"
        is AppException.ParseError -> "Ошибка обработки данных"
        is AppException.LocationPermissionDenied -> "Нет доступа к геолокации"
        is AppException.GpsDisabled -> "GPS выключен"
        is AppException.Unknown -> message
    }
}