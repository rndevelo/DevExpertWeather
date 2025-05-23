package com.rndeveloper.myapplication.framework.weather.remote

import com.rndeveloper.myapplication.data.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.domain.weather.model.Weather
import jakarta.inject.Inject

internal class WeatherServerDataSource @Inject constructor(private val weatherClient: WeatherService) : WeatherRemoteDataSource {

    override suspend fun getWeather(lat: Double, lon: Double): Weather =
        weatherClient
            .fetchWeather(lat = lat, lon = lon)
            .toDomainModel(lat = lat, lon = lon)
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