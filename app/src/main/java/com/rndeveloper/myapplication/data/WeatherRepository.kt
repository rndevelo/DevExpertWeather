package com.rndeveloper.myapplication.data

class WeatherRepository {

    suspend fun getWeather(locality: String): CurrentWeather =
        WeatherClient
            .instance
            .fetchWeather(locality)
            .toDomainModel()

}

private fun RemoteResult.toDomainModel(): CurrentWeather {
    return CurrentWeather(
        cityName = this.location.name,
        country = this.location.country,
        localTime = this.location.localTime,
        temperature = this.current.temperature,
        humidity = this.current.humidity,
        windSpeed = this.current.windSpeed,
        weatherDescriptions = this.current.weatherDescriptions,
        weatherIcons = this.current.weatherIcons,
    )

}