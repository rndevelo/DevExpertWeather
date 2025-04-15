package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.datasource.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.domain.City
import com.rndeveloper.myapplication.domain.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) {

    fun weather(lat: Double, lon: Double): Flow<Weather> = flow {
        val localWeather = weatherLocalDataSource.weather(lat, lon).firstOrNull()

        if (localWeather == null || shouldFetchRemote(localWeather.lastUpdated)) {
            val remoteWeather = weatherRemoteDataSource.getWeather(lat, lon)
            weatherLocalDataSource.insertWeather(remoteWeather)
        }

        emitAll(weatherLocalDataSource.weather(lat, lon).filterNotNull())
    }

    private fun shouldFetchRemote(lastUpdated: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000 // 10 minutos en milisegundos
        return (currentTime - lastUpdated) > tenMinutesInMillis
    }

    val favCities: Flow<List<City>> = flow { emitAll(weatherLocalDataSource.favCities) }

    suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            weatherLocalDataSource.deleteFavCity(city)
        } else {
            weatherLocalDataSource.insertFavCity(city)
        }
    }
}

