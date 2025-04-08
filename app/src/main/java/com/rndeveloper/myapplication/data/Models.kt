package com.rndeveloper.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val current: CurrentWeather,
    val forecast: List<DailyForecast>
)


data class CurrentWeather(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitation: Double,
)


// Nueva clase para representar el pronóstico diario de forma individual
data class DailyForecast(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val precipitation: Double,
)