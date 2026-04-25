package com.example.weatherapp.data.mapper


import com.example.weatherapp.data.local.entity.DailyForecastEntity
import com.example.weatherapp.data.local.entity.HourlyForecastEntity
import com.example.weatherapp.data.local.entity.WeatherEntity
import com.example.weatherapp.data.remote.dto.CurrentDto
import com.example.weatherapp.data.remote.dto.DailyDto
import com.example.weatherapp.data.remote.dto.HourlyDto
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather

fun CurrentDto.toEntity(cityName: String): WeatherEntity = WeatherEntity(
    id = 1,
    cityName = cityName,
    description = weather.firstOrNull()?.description ?: "",
    main = weather.firstOrNull()?.main ?: "",
    iconId = weather.firstOrNull()?.icon ?: "",
    temperature = temp,
    feelsLike = feelsLike,
    pressure = pressure,
    humidity = humidity,
    windSpeed = windSpeed,
    windGust = windGust,
    windDirection = windDeg,
    sunrise = sunrise * 1000,
    sunset = sunset * 1000,
    tempMin = temp,
    tempMax = temp,
    visibility = visibility,
    uvIndex = uvi,
    dewPoint = dewPoint,
    updatedAt = System.currentTimeMillis(),
)

fun WeatherEntity.toDomain(): Weather = Weather(
    cityName = cityName,
    description = description,
    main = main,
    iconId = iconId,
    temperature = temperature,
    feelsLike = feelsLike,
    pressure = pressure,
    humidity = humidity,
    windSpeed = windSpeed,
    windGust = windGust,
    windDirection = windDirection,
    sunrise = sunrise,
    sunset = sunset,
    tempMin = tempMin,
    tempMax = tempMax,
    visibility = visibility,
    updatedAt = updatedAt,
    uvIndex = uvIndex,
    dewPoint = dewPoint
)

fun HourlyDto.toHourlyEntity(): HourlyForecastEntity = HourlyForecastEntity(
    timeInMillis = dt * 1000,
    temperature = temp,
    main = weather.firstOrNull()?.main ?: "",
    icon = weather.firstOrNull()?.icon ?: "",
)


fun HourlyForecastEntity.toDomain(): HourlyForecast = HourlyForecast(
    timeInMillis = timeInMillis,
    temperature = temperature,
    main = main,
    icon = icon,
)

fun DailyDto.toDailyEntity(): DailyForecastEntity = DailyForecastEntity(
    dateInMillis = dt * 1000,
    minTemp = temp.min,
    maxTemp = temp.max,
    main = weather.firstOrNull()?.main ?: "",
    icon = weather.firstOrNull()?.icon ?: "",
    summary = summary,
)

fun DailyForecastEntity.toDomain(): DailyForecast = DailyForecast(
    dateInMillis = dateInMillis,
    minTemp = minTemp,
    maxTemp = maxTemp,
    main = main,
    icon = icon,
    summary = summary,
)