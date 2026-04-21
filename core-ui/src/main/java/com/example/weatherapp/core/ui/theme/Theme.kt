package com.example.weatherapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val LightColors = lightColorScheme(
    primary = PrimaryColors.SkyBlueDark,
    onPrimary = PrimaryColors.White,
    secondary = PrimaryColors.CardBlue,
    onSecondary = PrimaryColors.White,
    background = PrimaryColors.SkyBlueLight,
    onBackground = PrimaryColors.White,
    surface = PrimaryColors.CardBlue,
    onSurface = PrimaryColors.White,
)

private val DarkColors = darkColorScheme(
    primary = PrimaryColors.CardBlueDark,
    onPrimary = PrimaryColors.White,
    secondary = PrimaryColors.CardBlue,
    onSecondary = PrimaryColors.White,
    background = PrimaryColors.CardBlueDark,
    onBackground = PrimaryColors.White,
    surface = PrimaryColors.CardBlue,
    onSurface = PrimaryColors.White,
)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            typography = PrimaryTypography,
            shapes = PrimaryShapes,
            content = content,
        )
    }
}