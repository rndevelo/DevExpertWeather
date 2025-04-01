package com.rndeveloper.myapplication.data.datasource

import android.util.Log
import com.rndeveloper.myapplication.data.datasource.database.CitiesInfoDao
import com.rndeveloper.myapplication.data.datasource.remote.City

class CitiesInfoLocalDataSource(private val citiesInfoDao: CitiesInfoDao) {

    // Métodos para interactuar con la base de datos local usando el DAO
    suspend fun getAllCities(): List<City> {
        Log.d("Room", "getAllCities: ${citiesInfoDao.getAllCities()}")
        return citiesInfoDao.getAllCities()
    }

    suspend fun getCityByName(name: String): City = citiesInfoDao.getCityByName(name)

    suspend fun isEmpty() = citiesInfoDao.countCities() <= 0

    suspend fun insertCity(city: City) = citiesInfoDao.insertCity(city)
}
