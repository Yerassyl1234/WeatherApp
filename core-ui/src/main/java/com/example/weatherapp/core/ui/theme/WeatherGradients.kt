package com.example.weatherapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun getWeatherGradient(iconId: String?): Brush {
    val colors = when {
        iconId == null -> listOf(PrimaryColors.SkyBlueDark, PrimaryColors.SkyBlueLight)
        iconId.contains("n") -> listOf(Color(0xFF0B1021), Color(0xFF2B2D42))
        iconId.startsWith("01") || iconId.startsWith("02") -> listOf(Color(0xFF4A90E2), Color(0xFF50E3C2))
        iconId.startsWith("09") || iconId.startsWith("10") || iconId.startsWith("11") -> listOf(Color(0xFF4B5358), Color(0xFF545C6A))
        iconId.startsWith("13") -> listOf(Color(0xFF83A4D4), Color(0xFFB6FBFF))
        else -> listOf(PrimaryColors.SkyBlueDark, PrimaryColors.SkyBlueLight)
    }
    return Brush.verticalGradient(colors = colors)
}