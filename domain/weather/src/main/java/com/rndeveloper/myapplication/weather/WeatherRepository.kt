package com.rndeveloper.myapplication.weather

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    fun weather(lat: Double, lon: Double): Flow<Weather>
    val favCities: Flow<List<City>>
    suspend fun searchCities(query: String): List<City>
    suspend fun toggleFavCity(city: City, isFav: Boolean)
}