package com.example.weatherapp.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.weatherapp.core.ui.R

enum class BottomNavItem(
    val screen: Screen,
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int,
) {
    Map(
        screen = Screen.Map,
        iconRes = R.drawable.tabbar_map,
        labelRes = R.string.tab_map,
    ),
    Main(
        screen = Screen.Main,
        iconRes = R.drawable.tabbar_detail,
        labelRes = R.string.tab_weather,
    ),
    Cities(
        screen = Screen.Cities,
        iconRes = R.drawable.tabbar_cities,
        labelRes = R.string.tab_cities
    ),
}