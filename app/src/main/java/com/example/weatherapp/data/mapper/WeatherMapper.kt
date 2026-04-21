package com.example.weatherapp.data.mapper


import com.example.weatherapp.data.locale.entity.DailyForecastEntity
import com.example.weatherapp.data.locale.entity.HourlyForecastEntity
import com.example.weatherapp.data.locale.entity.WeatherEntity
import com.example.weatherapp.data.remote.dto.ForecastItemDto
import com.example.weatherapp.data.remote.dto.ForecastResponseDto
import com.example.weatherapp.data.remote.dto.WeatherDto
import com.example.weatherapp.domain.model.DailyForecast
import com.example.weatherapp.domain.model.HourlyForecast
import com.example.weatherapp.domain.model.Weather

fun WeatherDto.toEntity(): WeatherEntity = WeatherEntity(
    id = 1,
    cityName = cityName,
    description = weather.firstOrNull()?.description ?: "",
    main = weather.firstOrNull()?.main ?: "",
    iconId = weather.firstOrNull()?.icon ?: "",
    temperature = main.temp,
    feelsLike = main.feelsLike,
    pressure = main.pressure,
    humidity = main.humidity,
    windSpeed = wind.speed,
    windGust = wind.gust,
    windDirection = wind.deg,
    sunrise = sys?.sunrise?.times(1000) ?: 0L,
    sunset = sys?.sunset?.times(1000) ?: 0L,
    tempMin = main.tempMin,
    tempMax = main.tempMax,
    visibility = visibility,
    updatedAt = System.currentTimeMillis(),
)

fun ForecastResponseDto.toHourlyEntities(): List<HourlyForecastEntity> =
    list.map { item ->
        HourlyForecastEntity(
            timeInMillis = item.dt * 1000,
            temperature = item.main.temp,
            main = item.weather.firstOrNull()?.main ?: "",
            icon = item.weather.firstOrNull()?.icon ?: "",
        )
    }

fun ForecastResponseDto.toDailyEntities(): List<DailyForecastEntity> =
    list.groupBy { item ->
        item.dt / 86400
    }.map { (_, dayItems) ->
        DailyForecastEntity(
            dateInMillis = dayItems.first().dt * 1000,
            minTemp = dayItems.minOf { it.main.tempMin },
            maxTemp = dayItems.maxOf { it.main.tempMax },
            main = dayItems.mostFrequentMain(),
            icon = dayItems.mostFrequentIcon(),
        )
    }

private fun List<ForecastItemDto>.mostFrequentMain(): String =
    mapNotNull { it.weather.firstOrNull()?.main }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: ""

private fun List<ForecastItemDto>.mostFrequentIcon(): String =
    mapNotNull { it.weather.firstOrNull()?.icon?.dropLast(1)?.plus("d") }
        .groupingBy { it }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key ?: ""

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
)

fun HourlyForecastEntity.toDomain(): HourlyForecast = HourlyForecast(
    timeInMillis = timeInMillis,
    temperature = temperature,
    main = main,
    icon = icon,
)

fun DailyForecastEntity.toDomain(): DailyForecast = DailyForecast(
    dateInMillis = dateInMillis,
    minTemp = minTemp,
    maxTemp = maxTemp,
    main = main,
    icon = icon,
)