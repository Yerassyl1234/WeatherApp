package com.example.weatherapp.presentation.screens.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.component.WeatherBackground
import com.example.weatherapp.core.ui.component.WeatherCardTitle
import com.example.weatherapp.core.ui.component.WeatherInfoCard
import com.example.weatherapp.core.ui.theme.spacing
import com.example.weatherapp.core.ui.theme.weatherColors
import com.example.weatherapp.core.ui.util.setWeatherBg
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather
import com.example.weatherapp.presentation.common.toDayString
import com.example.weatherapp.presentation.common.toTimeString
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.onAction(MainAction.LoadWeather())
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.onAction(MainAction.LoadWeather())
        } else {
            permissionLauncher.launch(ACCESS_FINE_LOCATION)
        }
    }

    WeatherBackground(
        background = setWeatherBg(
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            isRaining = state.weather?.main == "Rain",
        ),
    ) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null && state.weather == null -> {
                ErrorScreen(
                    message = state.error!!.asString(),
                    onRetry = { viewModel.onAction(MainAction.LoadWeather()) },
                )
            }
            state.weather != null -> {
                CollapsingWeatherLayout(
                    state = state,
                    onAction = { viewModel.onAction(it) },
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollapsingWeatherLayout(
    state: MainUiState,
    onAction: (MainAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val weather = state.weather ?: return
    val density = LocalDensity.current

    val maxHeightDp = 340.dp
    val minHeightDp = 140.dp
    val maxHeightPx = with(density) { maxHeightDp.toPx() }
    val minHeightPx = with(density) { minHeightDp.toPx() }

    var headerOffset by remember { mutableFloatStateOf(0f) }
    val listState = rememberLazyListState()

    val fraction by remember {
        derivedStateOf {
            (headerOffset / (maxHeightPx - minHeightPx)).coerceIn(0f, 1f)
        }
    }

    val nestedScrollConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                if (delta < 0) {
                    if (headerOffset < maxHeightPx - minHeightPx) {
                        val oldOffset = headerOffset
                        headerOffset = (headerOffset - delta).coerceIn(0f, maxHeightPx - minHeightPx)
                        val consumed = oldOffset - headerOffset
                        return Offset(0f, consumed)
                    }
                    return Offset.Zero
                }

                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                if (delta > 0 && headerOffset > 0f) {
                    val oldOffset = headerOffset
                    headerOffset = (headerOffset - delta).coerceIn(0f, maxHeightPx - minHeightPx)
                    val consumed = oldOffset - headerOffset
                    return Offset(0f, consumed)
                }

                return Offset.Zero
            }
        }
    }

    val currentHeaderHeight = with(density) {
        (maxHeightPx - headerOffset).toDp()
    }

    val navigationBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        CompositionLocalProvider(LocalContentColor provides Color.White) {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = { onAction(MainAction.LoadWeather(isRefreshing = true)) },
                modifier = Modifier.clipToBounds(),
            ){
            Column(Modifier.fillMaxSize()) {
                CollapsibleHeader(
                    weather = weather,
                    fraction = fraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(currentHeaderHeight),
                )
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 8.dp + navigationBarPadding,
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            HourlyForecastSection(
                                forecasts = state.hourlyForecast,
                                summary = state.dailyForecast.firstOrNull()?.summary,
                            )
                        }
                        item { DailyForecastSection(state.dailyForecast) }
                        item { TemperatureSection(weather) }
                        item { WindSection(weather) }
                        item { UvAndSunsetSection(weather) }
                        item { HumidityAndPressureSection(weather) }
                    }
                }
            }
        }
    }
}

@Composable
fun CollapsibleHeader(
    weather: Weather,
    fraction: Float,
    modifier: Modifier = Modifier,
) {
    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val tempFontSize = lerp(96.sp, 20.sp, fraction)
    val expandedAlpha = (1f - fraction * 2.5f).coerceIn(0f, 1f)

    Box(
        modifier = modifier.padding(top = statusBarPadding),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.location_label),
                style = MaterialTheme.typography.labelSmall,
            )

            Text(
                text = weather.cityName,
                style = MaterialTheme.typography.headlineMedium,
            )

            if (fraction < 1f) {
                Text(
                    text = stringResource(
                        R.string.core_ui_temperature_format,
                        weather.temperature.roundToInt(),
                    ),
                    fontSize = tempFontSize,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.alpha(expandedAlpha),
                )
                Text(
                    text = weather.description.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.alpha(expandedAlpha),
                )
                Text(
                    text = stringResource(
                        R.string.core_ui_temp_min_max,
                        weather.tempMax.roundToInt(),
                        weather.tempMin.roundToInt(),
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.alpha(expandedAlpha),
                )
            }

            if (fraction > 0.5f) {
                Text(
                    text = stringResource(
                        R.string.collapsed_header,
                        weather.temperature.roundToInt(),
                        weather.description,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha((fraction - 0.5f) * 2f),
                )
            }
        }
    }
}

@Composable
private fun HourlyForecastSection(
    forecasts: List<HourlyForecast>,
    summary: String?,
    modifier: Modifier = Modifier,
) {
    WeatherInfoCard(
        modifier=modifier.fillMaxWidth()
    ){
        if (summary != null) {
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
            )

            HorizontalDivider(
                color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(forecasts.size){ index->
                HourlyItem(
                    forecast = forecasts[index],
                    isFirst = index == 0
                )
            }
        }
    }
}

@Composable
private fun HourlyItem(
    forecast: HourlyForecast,
    isFirst:Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Text(
            text = if (isFirst) {
                stringResource(R.string.now)
            } else {
                forecast.timeInMillis.toTimeString()
            },
            style = MaterialTheme.typography.labelMedium,
        )
        AsyncImage(
            model = stringResource(R.string.icon_url, forecast.icon),
            contentDescription = forecast.main,
            modifier = Modifier.size(32.dp),
        )
        Text(
            text = stringResource(
                R.string.core_ui_temperature_format,
                forecast.temperature.roundToInt()
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DailyForecastSection(
    forecasts: List<DailyForecast>,
    modifier: Modifier = Modifier,
) {
    WeatherInfoCard(
        modifier = modifier.fillMaxWidth()
    ){
        WeatherCardTitle(
            icon = painterResource(R.drawable.calendar),
            title = stringResource(R.string.core_ui_daily_forecast_title)
        )

        HorizontalDivider(
            color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
        )

        forecasts.forEachIndexed { index, forecast ->
            DailyItem(
                forecast = forecast,
                isToday = index == 0,
            )

            if (index < forecasts.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
                )
            }
        }
    }
}

@Composable
private fun DailyItem(
    forecast: DailyForecast,
    isToday: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = if (isToday) {
                stringResource(R.string.today)
            } else {
                forecast.dateInMillis.toDayString()
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
        AsyncImage(
            model = stringResource(R.string.icon_url, forecast.icon),
            contentDescription = forecast.main,
            modifier = Modifier.size(28.dp),
        )
        Text(
            text = stringResource(
                R.string.core_ui_temperature_format,
                forecast.minTemp.roundToInt()
            ),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.weatherColors.textSecondary,
            ),
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End,
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Text(
            text = stringResource(
                R.string.core_ui_temperature_format,
                forecast.maxTemp.roundToInt()
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun TemperatureSection(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ){
        WeatherInfoCard(modifier = Modifier.weight(1f).fillMaxHeight()){
            WeatherCardTitle(
                icon = painterResource(R.drawable.graph),
                title = stringResource(R.string.average_title),
            )
            Text(
                text = stringResource(
                    R.string.average_value,
                    weather.tempMax.roundToInt(),
                ),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = stringResource(R.string.average_description),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.weatherColors.textSecondary
                )
            )
        }
        WeatherInfoCard(modifier = modifier.weight(1f).fillMaxHeight()){
            WeatherCardTitle(
                icon = painterResource(R.drawable.thermometer),
                title = stringResource(R.string.feels_like_title),
            )
            Text(
                text = stringResource(
                    R.string.core_ui_temperature_format,
                    weather.feelsLike.roundToInt()
                ),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.feels_like_description),
                style = MaterialTheme.typography.bodySmall.copy(
                    color= MaterialTheme.weatherColors.textSecondary,
                ),
            )
        }
    }
}

@Composable
fun WindSection(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    WeatherInfoCard(modifier = modifier.fillMaxWidth()) {
        WeatherCardTitle(
            icon = painterResource(R.drawable.wind),
            title = stringResource(R.string.wind_title),
        )

        WindRow(
            label = stringResource(R.string.wind_speed),
            value = stringResource(
                R.string.wind_value_kmh,
                weather.windSpeed.roundToInt()
            ),
        )

        HorizontalDivider(color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f))

        WindRow(
            label = stringResource(R.string.wind_gust),
            value = stringResource(
                R.string.wind_value_kmh,
                (weather.windGust ?: 0.0).roundToInt()
            ),
        )

        HorizontalDivider(color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f))

        WindRow(
            label = stringResource(R.string.wind_direction),
            value = stringResource(
                R.string.wind_direction_value,
                weather.windDirection
            ),
        )
    }
}

@Composable
private fun WindRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.small),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun UvAndSunsetSection(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            MaterialTheme.spacing.medium
        ),
    ) {
        WeatherInfoCard(modifier = Modifier.weight(1f)) {
            WeatherCardTitle(
                icon = painterResource(R.drawable.sun),
                title = stringResource(R.string.uv_title),
            )

            Text(
                text = weather.uvIndex.roundToInt().toString(),
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = getUvLevelText(weather.uvIndex),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        WeatherInfoCard(modifier = Modifier.weight(1f)) {
            WeatherCardTitle(
                icon = painterResource(R.drawable.sunset),
                title = stringResource(R.string.sunset_title),
            )

            Text(
                text = weather.sunset.toTimeString(),
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = stringResource(
                    R.string.sunrise_label,
                    weather.sunrise.toTimeString()
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.weatherColors.textSecondary,
                ),
            )
        }
    }
}

@Composable
private fun getUvLevelText(uvIndex: Double): String {
    return when {
        uvIndex <= 2 -> stringResource(R.string.uv_low)
        uvIndex <= 5 -> stringResource(R.string.uv_moderate)
        uvIndex <= 7 -> stringResource(R.string.uv_high)
        uvIndex <= 10 -> stringResource(R.string.uv_very_high)
        else -> stringResource(R.string.uv_extreme)
    }
}

@Composable
fun HumidityAndPressureSection(
    weather: Weather,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
    ) {
        WeatherInfoCard(modifier = Modifier.weight(1f)) {
            WeatherCardTitle(
                icon = painterResource(R.drawable.humidity),
                title = stringResource(R.string.humidity_title),
            )

            Text(
                text = stringResource(
                    R.string.humidity_value,
                    weather.humidity
                ),
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = stringResource(
                    R.string.dew_point_label,
                    weather.dewPoint.roundToInt()
                ),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.weatherColors.textSecondary,
                ),
            )
        }

        WeatherInfoCard(modifier = Modifier.weight(1f)) {
            WeatherCardTitle(
                icon = painterResource(R.drawable.squeeze),
                title = stringResource(R.string.pressure_title),
            )

            Text(
                text = weather.pressure.toString(),
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = stringResource(R.string.pressure_unit),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.weatherColors.textSecondary,
                ),
            )
        }
    }
}