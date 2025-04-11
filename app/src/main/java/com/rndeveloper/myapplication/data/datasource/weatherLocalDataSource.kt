package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.datasource.database.WeatherDao
import com.rndeveloper.myapplication.data.datasource.remote.City

class WeatherLocalDataSource(private val weatherDao: WeatherDao) {

    // Métodos para interactuar con la base de datos local usando el DAO
    val favCities = weatherDao.getFavCities()

    fun weather(lat: Double, lon: Double) = weatherDao.getWeather(lat, lon)

    suspend fun insertWeather(weather: Weather) = weatherDao.insertWeather(weather)

    suspend fun insertFavCity(city: City) = weatherDao.insertFavCity(city)

    suspend fun deleteFavCity(city: City) = weatherDao.deleteFavCity(city)
}
