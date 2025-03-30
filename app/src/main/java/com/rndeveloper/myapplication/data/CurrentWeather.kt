package com.rndeveloper.myapplication.data

//data class Weather(
//    val currentWeather: CurrentWeather,
//    val forecast: List<CurrentWeather>
//)
//
//data class CurrentWeather(
//    val localTime: String,
//    val temperature: Double,
//    val humidity: Int,
//    val windSpeed: Double,
//    val weatherDescriptions: List<String>,
//    val weatherIcons: List<String>,
//)

// Modelo de datos para el pronóstico del tiempo
data class WeatherForecast(
    val date: String,
    val iconUrl: String,
    val maxTemp: Int,
    val minTemp: Int,
    val description: String
)

