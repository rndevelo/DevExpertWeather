package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.datasource.CitiesInfoLocalDataSource
import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.data.datasource.remote.City

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: CitiesInfoLocalDataSource
) {

    suspend fun getWeather(lat: Double, lon: Double) = weatherRemoteDataSource.getWeather(lat, lon)

    suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    suspend fun getFavCities() = localDataSource.getAllCities()

    suspend fun insertCity(city: City) = localDataSource.insertCity(city)
}

