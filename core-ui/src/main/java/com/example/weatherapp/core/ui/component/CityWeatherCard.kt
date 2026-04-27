package com.example.weatherapp.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.theme.WeatherAppTheme
import com.example.weatherapp.core.ui.theme.spacing
import com.example.weatherapp.core.ui.theme.weatherColors
import com.example.weatherapp.core.ui.util.WeatherBg
import com.example.weatherapp.core.ui.util.toDrawable

@Composable
fun CityWeatherCard(
    cityName: String,
    subtitle: String,
    description: String,
    temperature: Int,
    tempMin: Int,
    tempMax: Int,
    background: WeatherBg,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Box {
            Image(
                painter = painterResource(id = background.toDrawable()),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(MaterialTheme.spacing.medium),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cityName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.weatherColors.textPrimary,
                    )

                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.weatherColors.weatherRain,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.weatherColors.weatherRain,
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.core_ui_temperature_format, temperature),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.weatherColors.textPrimary,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = stringResource(R.string.core_ui_temp_min_max, tempMax, tempMin),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.weatherColors.textSecondary,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CityWeatherCardPreview() {
    WeatherAppTheme {
        CityWeatherCard(
            cityName = "Алматы",
            subtitle = "Текущее место · Работа",
            description = "В основном солнечно",
            temperature = 11,
            tempMin = 5,
            tempMax = 11,
            background = WeatherBg.DAY,
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}