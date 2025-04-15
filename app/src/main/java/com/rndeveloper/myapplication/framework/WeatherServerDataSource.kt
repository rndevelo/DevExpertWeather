package com.rndeveloper.myapplication.framework

import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.domain.City
import com.rndeveloper.myapplication.domain.Weather
import com.rndeveloper.myapplication.framework.remote.RemoteWeather
import com.rndeveloper.myapplication.framework.remote.WeatherService

class WeatherServerDataSource(private val weatherClient: WeatherService) : WeatherRemoteDataSource {

    override suspend fun getWeather(lat: Double, lon: Double): Weather =
        weatherClient
            .fetchWeather(lat = lat, lon = lon)
            .toDomainModel(lat = lat, lon = lon)

    override suspend fun searchCities(query: String): List<City> {
        return try {
            val response = weatherClient.searchCities(query)
            response.results.map { it.toCity() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

private fun RemoteWeather.toDomainModel(lat: Double, lon: Double): Weather {
    return Weather(
        current = this.current.toCurrent(),
        forecast = this.daily.toDailyForecastList(),
        lastUpdated = System.currentTimeMillis(),
        lat = lat,
        lon = lon
    )
}