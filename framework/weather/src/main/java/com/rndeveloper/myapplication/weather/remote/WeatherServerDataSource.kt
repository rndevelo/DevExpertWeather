package com.rndeveloper.myapplication.weather.remote

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.weather.model.Weather

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