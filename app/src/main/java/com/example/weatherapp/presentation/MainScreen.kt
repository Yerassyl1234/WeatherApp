package com.example.weatherapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.component.WeatherCardTitle
import com.example.weatherapp.core.ui.component.WeatherInfoCard
import com.example.weatherapp.core.ui.theme.PrimaryColors
import com.example.weatherapp.core.ui.theme.getWeatherGradient
import com.example.weatherapp.core.ui.util.toDayString
import com.example.weatherapp.core.ui.util.toTimeString
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let { errorMsg ->
            val result = snackbarHostState.showSnackbar(
                message = errorMsg,
                actionLabel = "ОК"
            )
            if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(getWeatherGradient(state.weather?.iconId))
        ) {
            if (state.isLoading && state.weather == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryColors.White
                )
            } else {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { viewModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            state.weather?.let { HeaderSection(it) }
                        }

                        item {
                            WeatherInfoCard {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(state.hourlyForecast) { hour ->
                                        HourlyItem(hour)
                                    }
                                }
                            }
                        }

                        item {
                            WeatherInfoCard {
                                WeatherCardTitle(
                                    title = "ПРОГНОЗ НА 5 ДНЕЙ",
                                    icon = painterResource(R.drawable.calendar)
                                )
                                state.dailyForecast.forEachIndexed { index, day ->
                                    DailyItem(day)
                                    if (index < state.dailyForecast.lastIndex) {
                                        HorizontalDivider(color = PrimaryColors.DividerWhite)
                                    }
                                }
                            }
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ОЩУЩАЕТСЯ КАК",
                                        icon = painterResource(R.drawable.thermometer)
                                    )
                                    Text(
                                        text = "${state.weather?.feelsLike?.toInt()}°",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ВЛАЖНОСТЬ",
                                        icon = painterResource(R.drawable.humidity)
                                    )
                                    Text(
                                        text = "${state.weather?.humidity}%",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                            }
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ВЕТЕР",
                                        icon = painterResource(R.drawable.wind)
                                    )
                                    Text(
                                        text = "${state.weather?.windSpeed?.toInt()} м/с",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ДАВЛЕНИЕ",
                                        icon = painterResource(R.drawable.squeeze)
                                    )
                                    Text(
                                        text = "${state.weather?.pressure} гПа",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                            }
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ВИДИМОСТЬ",
                                        icon = painterResource(R.drawable.sun)
                                    )
                                    Text(
                                        text = "${(state.weather?.visibility ?: 0) / 1000} км",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                                WeatherInfoCard(modifier = Modifier.weight(1f)) {
                                    WeatherCardTitle(
                                        title = "ЗАКАТ",
                                        icon = painterResource(R.drawable.sunset)
                                    )
                                    Text(
                                        text = state.weather?.sunset?.toTimeString() ?: "",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryColors.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(weather: Weather) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weather.cityName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryColors.White
        )
        Text(
            text = "${weather.temperature.toInt()}°",
            fontSize = 72.sp,
            fontWeight = FontWeight.Thin,
            color = PrimaryColors.White
        )
        Text(
            text = weather.description.replaceFirstChar { it.uppercase() },
            fontSize = 18.sp,
            color = PrimaryColors.WhiteAlpha70
        )
        Text(
            text = "Макс.: ${weather.tempMax.toInt()}°, мин.: ${weather.tempMin.toInt()}°",
            fontSize = 18.sp,
            color = PrimaryColors.WhiteAlpha70
        )
    }
}

@Composable
private fun HourlyItem(forecast: HourlyForecast) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = forecast.timeInMillis.toTimeString(),
            fontSize = 14.sp,
            color = PrimaryColors.WhiteAlpha70
        )
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png",
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = "${forecast.temperature.toInt()}°",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = PrimaryColors.White
        )
    }
}

@Composable
private fun DailyItem(forecast: DailyForecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = forecast.dateInMillis.toDayString(),
            fontSize = 16.sp,
            color = PrimaryColors.White,
            modifier = Modifier.weight(1f)
        )
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End) {
            AsyncImage(
                model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png",
                contentDescription = null,
                modifier = Modifier.size(32.dp).padding(end = 8.dp)
            )
            Text(
                text = "${forecast.minTemp.toInt()}°",
                fontSize = 16.sp,
                color = PrimaryColors.WhiteAlpha50
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${forecast.maxTemp.toInt()}°",
                fontSize = 16.sp,
                color = PrimaryColors.White
            )
        }
    }
}