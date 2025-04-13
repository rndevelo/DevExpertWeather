package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.datasource.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.data.datasource.remote.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource,
) {

    fun weather(lat: Double, lon: Double): Flow<Weather> = flow {
        val localWeather = weatherLocalDataSource.weather(lat, lon).firstOrNull()
        if (localWeather == null || shouldFetchRemote(localWeather.lastUpdated)) {
            val remoteWeather = weatherRemoteDataSource.getWeather(lat, lon)
            weatherLocalDataSource.insertWeather(remoteWeather)
        }
        emitAll(weatherLocalDataSource.weather(lat, lon))
    }

    private fun shouldFetchRemote(lastUpdated: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000 // 10 minutos en milisegundos
        return (currentTime - lastUpdated) > tenMinutesInMillis
    }

    val favCities: Flow<List<City>> = weatherLocalDataSource.favCities.transform {
        val cities = it.takeIf { it.isNotEmpty() } ?: emptyList()
        emit(cities)
    }

    suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            weatherLocalDataSource.deleteFavCity(city)
        } else {
            weatherLocalDataSource.insertFavCity(city)
        }
    }
}

