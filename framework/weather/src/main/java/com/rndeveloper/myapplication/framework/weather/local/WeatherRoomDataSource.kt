package com.rndeveloper.myapplication.framework.weather.local

import com.rndeveloper.myapplication.data.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.domain.weather.model.Weather
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class WeatherRoomDataSource @Inject constructor(private val weatherDao: WeatherDao) :
    WeatherLocalDataSource {

    override fun weather(lat: Double, lon: Double): Flow<Weather?> =
        weatherDao.getWeather(lat, lon).map { db -> db?.toWeather() }

    override suspend fun insertWeather(weather: Weather) =
        weatherDao.insertWeather(weather.toDbModel())
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

fun DbWeather.toWeather(): Weather {
    return Weather(
        current = current,
        forecast = forecast,
        lastUpdated = lastUpdated,
        lat = lat,
        lon = lon
    )
}
