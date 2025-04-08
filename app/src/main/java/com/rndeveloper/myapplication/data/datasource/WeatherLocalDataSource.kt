package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.datasource.database.CitiesDao
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val citiesDao: CitiesDao) {

//    val weather: Flow<Weather> = citiesDao.getWeather()

}