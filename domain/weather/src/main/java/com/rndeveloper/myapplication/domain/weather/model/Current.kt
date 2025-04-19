package com.rndeveloper.myapplication.domain.weather.model

data class Current(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitation: Double,
)
