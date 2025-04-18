package com.rndeveloper.myapplication.weather

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun weather(lat: Double, lon: Double): Flow<Weather?>
    suspend fun insertWeather(weather: Weather)

    val favCities: Flow<List<City>>
    suspend fun insertFavCity(city: City)
    suspend fun deleteFavCity(city: City)
}


