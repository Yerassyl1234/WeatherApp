package com.example.weatherapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColors = lightColorScheme(
    primary = WeatherPalette.SkyBlueDark,
    onPrimary = WeatherPalette.White,
    secondary = WeatherPalette.CardBlue,
    onSecondary = WeatherPalette.White,
    background = WeatherPalette.SkyBlueLight,
    onBackground = WeatherPalette.White,
    surface = WeatherPalette.CardBlue,
    onSurface = WeatherPalette.White,
)

private val DarkColors = darkColorScheme(
    primary = WeatherPalette.CardBlueDark,
    onPrimary = WeatherPalette.White,
    secondary = WeatherPalette.CardBlue,
    onSecondary = WeatherPalette.White,
    background = WeatherPalette.CardBlueDark,
    onBackground = WeatherPalette.White,
    surface = WeatherPalette.CardBlue,
    onSurface = WeatherPalette.White,
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val weatherColors = if (darkTheme) DarkWeatherColors else LightWeatherColors
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalWeatherColors provides weatherColors,
    )
    {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PrimaryTypography,
            shapes = PrimaryShapes,
            content = content,
        )
    }
}