package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.data.datasource.database.CitiesDao
import com.rndeveloper.myapplication.data.datasource.remote.City

class CitiesInfoLocalDataSource(private val citiesDao: CitiesDao) {

    // Métodos para interactuar con la base de datos local usando el DAO
    val favCities = citiesDao.getFavCities()

    fun getCityByName(name: String) = citiesDao.getCityByName(name)

    suspend fun isEmpty() = citiesDao.countCities() <= 0

    suspend fun insertCity(city: City) = citiesDao.insertCity(city)

    suspend fun deleteCity(city: City) = citiesDao.deleteCity(city)
}
