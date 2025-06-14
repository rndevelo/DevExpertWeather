package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun weather(lat: Double, lon: Double): Flow<Weather?>
    suspend fun insertWeather(weather: Weather)
}


