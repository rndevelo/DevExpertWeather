package com.rndeveloper.myapplication.domain.weather

import com.rndeveloper.myapplication.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    fun weather(lat: Double, lon: Double): Flow<Weather>
}