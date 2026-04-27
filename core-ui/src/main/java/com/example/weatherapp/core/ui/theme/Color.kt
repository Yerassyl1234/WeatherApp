package com.example.weatherapp.core.ui.theme


import android.graphics.Color.alpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class WeatherColors(
    val backgroundGradientTop: Color,
    val backgroundGradientBottom: Color,
    val card: Color,
    val divider: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val weatherSunny: Color,
    val weatherRain: Color,
)

internal val LightWeatherColors = WeatherColors(
    backgroundGradientTop = WeatherPalette.SkyBlueLight,
    backgroundGradientBottom = WeatherPalette.SkyBlueDark,
    card = WeatherPalette.CardBlue.copy(alpha = 0.3f),
    divider = WeatherPalette.White.copy(alpha = 0.2f),
    textPrimary = WeatherPalette.White,
    textSecondary = WeatherPalette.White.copy(alpha = 0.7f),
    textTertiary = WeatherPalette.White.copy(alpha = 0.5f),
    weatherSunny = WeatherPalette.SunnyYellow,
    weatherRain = WeatherPalette.RainBlue,
)

internal val DarkWeatherColors = WeatherColors(
    backgroundGradientTop = WeatherPalette.SkyBlueDark,
    backgroundGradientBottom = WeatherPalette.CardBlueDark,
    card = WeatherPalette.CardBlueDark.copy(alpha = 0.7f),
    divider = WeatherPalette.White.copy(alpha = 0.15f),
    textPrimary = WeatherPalette.White,
    textSecondary = WeatherPalette.White.copy(alpha = 0.7f),
    textTertiary = WeatherPalette.White.copy(alpha = 0.45f),
    weatherSunny = WeatherPalette.SunnyYellow,
    weatherRain = WeatherPalette.RainBlue,
)

internal val LocalWeatherColors = staticCompositionLocalOf<WeatherColors> {
    error("WeatherColors not provided. Wrap your composable in WeatherAppTheme.")
}

val MaterialTheme.weatherColors: WeatherColors
    @Composable
    @ReadOnlyComposable
    get() = LocalWeatherColors.current