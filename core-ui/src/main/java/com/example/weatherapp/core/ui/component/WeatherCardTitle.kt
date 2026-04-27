package com.example.weatherapp.core.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.example.weatherapp.core.ui.theme.spacing
import com.example.weatherapp.core.ui.theme.weatherColors

@Composable
fun WeatherCardTitle(
    title: String,
    icon: Painter,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(bottom = MaterialTheme.spacing.small)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = MaterialTheme.weatherColors.textTertiary,
            modifier = Modifier.size(MaterialTheme.spacing.medium)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
        Text(
            text = title.uppercase(),
            color = MaterialTheme.weatherColors.textTertiary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

