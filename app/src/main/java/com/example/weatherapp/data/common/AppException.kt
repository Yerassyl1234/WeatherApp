package com.example.weatherapp.data.common

sealed class AppException(message: String) : Exception(message) {
    object NoInternet : AppException("No internet connection")
    object Timeout : AppException("Request timed out")
    data class ServerError(val code: Int) : AppException("Server error: $code")
    data class ClientError(val code: Int) : AppException("Client error: $code")
    data class ParseError(override val message: String = "Failed to parse response") : AppException(message)
    object LocationPermissionDenied : AppException("Geolocation not allowed")
    object GpsDisabled : AppException("GPS is turned off")
    data class Unknown(override val message: String) : AppException(message)
}