package com.example.weatherapp.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import com.example.weatherapp.core.ui.theme.LocalSpacing
import com.example.weatherapp.core.ui.theme.PrimaryColors
import com.example.weatherapp.core.ui.theme.PrimaryShapes
import com.example.weatherapp.core.ui.theme.PrimaryTypography

@Composable
fun WeatherInfoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope. () -> Unit
) {
    Column(
        modifier = modifier
            .clip(PrimaryShapes.medium)
            .background(PrimaryColors.WhiteAlpha30)
            .padding(all = LocalSpacing.current.medium)
    ) {
        content()
    }
}

@Composable
fun WeatherCardTitle(
    title: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = LocalSpacing.current.small)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = PrimaryColors.WhiteAlpha50,
            modifier = Modifier.size(LocalSpacing.current.medium)
        )
        Spacer(modifier = Modifier.width(LocalSpacing.current.extraSmall))
        Text(
            text = title.uppercase(),
            color = PrimaryColors.WhiteAlpha50,
            style = PrimaryTypography.labelMedium
        )
    }
}