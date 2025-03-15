package com.rndeveloper.myapplication.data

data class CurrentWeather(
    val cityName: String,
    val country: String,
    val localTime: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherDescriptions: List<String>,
    val weatherIcons: List<String>,
)

