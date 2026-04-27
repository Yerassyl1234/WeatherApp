package com.example.weatherapp.presentation.common

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
@SuppressLint("ConstantLocale")
private val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.getDefault())

fun Long.toTimeString(): String =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(timeFormatter)

fun Long.toDayString(): String =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(dayFormatter)
        .replaceFirstChar { it.uppercase() }