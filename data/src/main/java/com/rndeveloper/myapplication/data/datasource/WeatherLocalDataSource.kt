package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.domain.City
import com.rndeveloper.myapplication.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun weather(lat: Double, lon: Double): Flow<Weather?>
    suspend fun insertWeather(weather: Weather)

    val favCities: Flow<List<City>>
    suspend fun insertFavCity(city: City)
    suspend fun deleteFavCity(city: City)
}


