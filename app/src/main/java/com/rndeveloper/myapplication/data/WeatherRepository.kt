package com.rndeveloper.myapplication.data

class WeatherRepository() {

    suspend fun getWeather(lat: Double, lon: Double): Weather =
        WeatherClient
            .instance
            .fetchWeather(lat = lat, lon = lon)
            .toDomainModel()

    suspend fun searchCities(query: String): List<CityInfo> {
        return try {
            val response = WeatherClient.instance.searchCities(query)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}

private fun RemoteWeather.toDomainModel(): Weather {
    return Weather(
        current = this.current.toCurrentWeather(),
        forecast = this.daily.toDailyForecastList()
    )
}