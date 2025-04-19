package com.rndeveloper.myapplication.domain.weather.model

// Nueva clase para representar el pronóstico diario de forma individual
data class DailyForecast(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val precipitation: Double,
)