package com.rndeveloper.myapplication.domain

import com.rndeveloper.myapplication.domain.weather.model.Current
import com.rndeveloper.myapplication.domain.weather.model.DailyForecast
import com.rndeveloper.myapplication.domain.weather.model.Weather

fun sampleWeather(lat: Double = -34.6037, lon: Double = -58.3816) = Weather(
    current = sampleCurrent(),
    forecast = sampleDailyForecastList(),
    lastUpdated = 1748357424355,
    lat = lat,
    lon = lon
)

fun sampleCurrent() = Current(
    date = "2023-09-25",
    weatherDescription = "Sunny",
    weatherIcon = "https://example.com/icon.png",
    temperature = 25.0,
    humidity = 60,
    windSpeed = 5.0,
    precipitation = 5.0
)

fun sampleDailyForecastList() = listOf<DailyForecast>(
    DailyForecast(
        date = "2023-09-25",
        weatherDescription = "Sunny",
        weatherIcon = "https://example.com/icon.png",
        maxTemperature = 25.0,
        minTemperature = 15.0,
        precipitation = 5.0
    ),
    DailyForecast(
        date = "2023-09-26",
        weatherDescription = "Cloudy",
        weatherIcon = "https://example.com/icon.png",
        maxTemperature = 25.0,
        minTemperature = 15.0,
        precipitation = 5.0
    )
)