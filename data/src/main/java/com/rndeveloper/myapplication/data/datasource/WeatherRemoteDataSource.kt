package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.domain.City
import com.rndeveloper.myapplication.domain.Weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(lat: Double, lon: Double): Weather
    suspend fun searchCities(query: String): List<City>
}
