package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.weather.model.Weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(lat: Double, lon: Double): Weather
    suspend fun searchCities(query: String): List<City>
}
