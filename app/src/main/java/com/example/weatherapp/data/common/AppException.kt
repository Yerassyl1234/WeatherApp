package com.example.weatherapp.data.common

sealed class AppException(
    message: String,
    cause: Throwable? = null,
) : Exception(message) {

    class NoInternet : AppException("No internet connection")
    class Timeout : AppException("Request timed out")

    data class ServerError(
        val code: Int
    ) : AppException("Server error: $code")

    data class ClientError(
        val code: Int,
        val serverMessage: String? = null
    ) : AppException("Client error: $code: ${serverMessage ?: "No details"}")

    data class ParseError(
        val originalException: Throwable
    ) : AppException("Failed to parse response", originalException)

    class LocationPermissionDenied : AppException("Geolocation not allowed")
    class GpsDisabled : AppException("GPS is turned off")

    data class Unknown(
        val errorMsg: String,
        val originalException: Throwable? = null
    ) : AppException(errorMsg, originalException)

}