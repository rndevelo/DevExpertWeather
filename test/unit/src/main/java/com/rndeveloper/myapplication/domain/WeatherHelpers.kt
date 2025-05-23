package com.rndeveloper.myapplication.domain

import com.rndeveloper.myapplication.domain.weather.model.Current
import com.rndeveloper.myapplication.domain.weather.model.DailyForecast
import com.rndeveloper.myapplication.domain.weather.model.Weather

fun sampleWeather(lat: Double, lon: Double) = Weather(
    current = sampleCurrent(),
    forecast = sampleDailyForecastList(),
    lastUpdated = System.currentTimeMillis(),
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
fun sampleDailyForecastList() = emptyList<DailyForecast>()