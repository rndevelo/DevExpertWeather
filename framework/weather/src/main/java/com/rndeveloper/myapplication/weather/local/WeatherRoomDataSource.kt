package com.rndeveloper.myapplication.weather.local

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.weather.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherRoomDataSource(private val weatherDao: WeatherDao) : WeatherLocalDataSource {

    // Métodos para interactuar con la base de datos local usando el DAO
    override fun weather(lat: Double, lon: Double): Flow<Weather?> =
        weatherDao.getWeather(lat, lon).map { db -> db?.toWeather() }

    override suspend fun insertWeather(weather: Weather) =
        weatherDao.insertWeather(weather.toDbModel())


    override val favCities = weatherDao.getFavCities().map { cities -> cities.map { it.toCity() } }
    override suspend fun insertFavCity(city: City) = weatherDao.insertFavCity(city.toDbModel())
    override suspend fun deleteFavCity(city: City) = weatherDao.deleteFavCity(city.toDbModel())
}

private fun Weather.toDbModel(): DbWeather {
    return DbWeather(
        current = current,
        forecast = forecast,
        lastUpdated = lastUpdated,
        lat = lat,
        lon = lon
    )
}

private fun City.toDbModel(): DbCity {
    return DbCity(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}

fun DbWeather.toWeather(): Weather {
    return Weather(
        current = current,
        forecast = forecast,
        lastUpdated = lastUpdated,
        lat = lat,
        lon = lon
    )
}

fun DbCity.toCity(): City {
    return City(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}