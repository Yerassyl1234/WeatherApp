package com.example.weatherapp.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

class LocationTracker(private val context: Context) {

    @SuppressLint("MissingPermission")
    suspend fun getLocation(): Location? {
        val client = LocationServices.getFusedLocationProviderClient(context)
        return client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
    }
}