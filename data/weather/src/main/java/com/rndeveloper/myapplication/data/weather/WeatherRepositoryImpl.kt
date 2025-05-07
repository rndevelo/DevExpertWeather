package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import com.rndeveloper.myapplication.domain.weather.model.Weather
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {


    override fun weather(lat: Double, lon: Double): Flow<Weather> = flow {
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

    override val favCities: Flow<List<City>> = flow { emitAll(weatherLocalDataSource.favCities) }

    override suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    override suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            weatherLocalDataSource.deleteFavCity(city)
        } else {
            weatherLocalDataSource.insertFavCity(city)
        }
    }
}

