package com.example.weatherapp.presentation.mapper


import com.example.weatherapp.core.ui.R
import com.example.weatherapp.core.ui.util.UiText
import com.example.weatherapp.data.common.AppException

internal fun AppException.toUiText(): UiText = when (this) {

    is AppException.ClientError -> {
        when (code) {
            401, 403 -> UiText.StringResource(R.string.core_ui_error_api_key)
            404 -> UiText.StringResource(R.string.core_ui_error_city_not_found)
            429 -> UiText.StringResource(R.string.core_ui_error_too_many_requests)
            else -> {
                if (!serverMessage.isNullOrBlank()) {
                    UiText.DynamicString(serverMessage)
                }
                else {
                    UiText.StringResource(R.string.core_ui_error_unknown)
                }
            }
        }
    }

    is AppException.GpsDisabled -> UiText.StringResource(R.string.core_ui_error_gps_disabled)
    is AppException.LocationPermissionDenied -> UiText.StringResource(R.string.core_ui_error_location_denied)
    is AppException.NoInternet -> UiText.StringResource(R.string.core_ui_error_no_internet)
    is AppException.ParseError -> UiText.StringResource(R.string.core_ui_error_unknown)

    is AppException.ServerError -> {
        when(code){
            500,502,503,504 -> UiText.StringResource(R.string.core_ui_error_server_down)
            else -> {
                UiText.StringResource(R.string.core_ui_error_server_with_code,code)
            }
        }
    }

    is AppException.Timeout -> UiText.StringResource(R.string.core_ui_error_timeout)

    is AppException.Unknown -> {
        if (errorMsg.isNotBlank()) UiText.DynamicString(errorMsg)
        else UiText.StringResource(R.string.core_ui_error_unknown)
    }
}