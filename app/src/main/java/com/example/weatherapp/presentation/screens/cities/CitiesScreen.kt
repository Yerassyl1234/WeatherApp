package com.example.weatherapp.presentation.screens.cities

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.component.CityWeatherCard
import com.example.weatherapp.core.ui.component.SearchBar
import com.example.weatherapp.core.ui.component.SettingsItem
import com.example.weatherapp.core.ui.component.WeatherBackground
import com.example.weatherapp.core.ui.theme.spacing
import com.example.weatherapp.core.ui.theme.weatherColors
import com.example.weatherapp.core.ui.util.setWeatherBg
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitiesScreen(
    onNavigateToWeather: (String) -> Unit,
    viewModel: CitiesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is CitiesEffect.NavigateToWeatherDetails -> {
                    onNavigateToWeather(effect.cityName)
                }
            }
        }
    }

    WeatherBackground(
        background = setWeatherBg(
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            isRaining = false,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding(),
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small,
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.cities_title),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.weatherColors.textPrimary,
                )

                Icon(
                    painter = painterResource(R.drawable.topbar),
                    contentDescription = null,
                    tint = MaterialTheme.weatherColors.textPrimary,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { showBottomSheet = true },
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.medium,
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            ) {
                items(state.cities) { city ->
                    CityWeatherCard(
                        cityName = city.cityName,
                        subtitle = city.time,
                        description = city.description,
                        temperature = city.temp,
                        tempMin = city.tempMin,
                        tempMax = city.tempMax,
                        background = city.background,
                        onClick = {
                            viewModel.onAction(
                                CitiesAction.CitySelected(city.cityName)
                            )
                        },
                    )
                }
            }

            SearchBar(
                query = state.query,
                onQueryChange = { viewModel.onAction(CitiesAction.InputSearch(it)) },
                modifier = Modifier
                    .padding(
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small,
                    )
                    .navigationBarsPadding()
                    .imePadding(),
            )
        }
    }

    if (showBottomSheet) {
        SettingsBottomSheet(
            onDismiss = { showBottomSheet = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsBottomSheet(
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.weatherColors.backgroundGradientBottom,
    ) {
        SettingsItem(
            iconRes = R.drawable.pencil,
            title = stringResource(R.string.settings_edit_list),
            onClick = {}
        )
        SettingsItem(
            iconRes = R.drawable.bell,
            title = stringResource(R.string.settings_notification),
            onClick = {}
        )
        HorizontalDivider(
            color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
        )
        SettingsItem(
            iconRes = R.drawable.celsius,
            title = stringResource(R.string.settings_celsius),
            onClick = {}
        )
        SettingsItem(
            iconRes = R.drawable.fahrenheit,
            title = stringResource(R.string.settings_fahrenheit),
            onClick = {}
        )
        HorizontalDivider(
            color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
        )
        SettingsItem(
            iconRes = R.drawable.colums,
            title = stringResource(R.string.settings_units),
            onClick = {}
        )
        HorizontalDivider(
            color = MaterialTheme.weatherColors.textSecondary.copy(alpha = 0.2f),
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
        )
        SettingsItem(
            iconRes = R.drawable.message,
            title = stringResource(R.string.settings_report),
            onClick = {}
        )
    }
}
