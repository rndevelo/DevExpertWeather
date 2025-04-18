package com.rndeveloper.myapplication.weather

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.model.Weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(lat: Double, lon: Double): Weather
    suspend fun searchCities(query: String): List<City>
}
