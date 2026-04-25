package com.example.weatherapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GeocodingDto(
    val name: String,
    @SerialName("local_names") val localNames: Map<String, String>? = null,
    val country: String,
)
