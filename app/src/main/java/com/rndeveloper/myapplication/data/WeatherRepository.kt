package com.rndeveloper.myapplication.data

import android.util.Log
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

        Log.d("WeatherRepository", "Trying to get local weather for $lat, $lon")

        val localWeather = weatherLocalDataSource.weather(lat, lon).firstOrNull()

        Log.d("WeatherRepository", "Local weather: $localWeather")

        if (localWeather == null || shouldFetchRemote(localWeather.lastUpdated)) {
            val remoteWeather = weatherRemoteDataSource.getWeather(lat, lon)
            Log.d("WeatherRepository", "Fetched remote: $remoteWeather")
            weatherLocalDataSource.insertWeather(remoteWeather)
        }

        emitAll(weatherLocalDataSource.weather(lat, lon))
    }

    private fun shouldFetchRemote(lastUpdated: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000 // 10 minutos en milisegundos
        val tenMinutesAgo = currentTime - lastUpdated
        Log.d(
            "WeatherRepository",
            "currentTime: $currentTime lastUpdated $lastUpdated tenMinutesAgo $tenMinutesAgo  10 min $tenMinutesInMillis"
        )

        return (currentTime - lastUpdated) > tenMinutesInMillis
    }

    suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    val favCities: Flow<List<City>> = weatherLocalDataSource.favCities.transform {
        val cities = it.takeIf { it.isNotEmpty() } ?: emptyList()
        emit(cities)
    }

    suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            weatherLocalDataSource.deleteFavCity(city)
        } else {
            weatherLocalDataSource.insertFavCity(city)
        }
    }
}

