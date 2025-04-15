package com.rndeveloper.myapplication.weather.model

data class Weather(
    val current: Current,
    val forecast: List<DailyForecast>,
    val lastUpdated: Long, // tiempo en milisegundos
    val lat: Double,
    val lon: Double
)