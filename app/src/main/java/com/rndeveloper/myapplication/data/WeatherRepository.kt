package com.rndeveloper.myapplication.data

class WeatherRepository {

    suspend fun getWeather(lat: Double, lon: Double): Weather =
        WeatherClient
            .instance
            .fetchWeather(lat = lat, lon = lon)
            .toDomainModel()

}

private fun RemoteWeather.toDomainModel(): Weather {
    return Weather(
        current = this.current.toCurrentWeather(),
        forecast = this.daily.toDailyForecastList()
    )
}