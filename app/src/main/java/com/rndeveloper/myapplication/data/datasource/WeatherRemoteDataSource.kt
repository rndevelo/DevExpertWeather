package com.rndeveloper.myapplication.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.rndeveloper.myapplication.data.datasource.remote.City
import com.rndeveloper.myapplication.data.datasource.remote.RemoteWeather
import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.datasource.remote.WeatherClient

class WeatherRemoteDataSource {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getWeather(lat: Double, lon: Double): Weather =
        WeatherClient
            .instance
            .fetchWeather(lat = lat, lon = lon)
            .toDomainModel()

    suspend fun searchCities(query: String): List<City> {
        return try {
            val response = WeatherClient.instance.searchCities(query)
            response.results
        } catch (e: Exception) {
            emptyList()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun RemoteWeather.toDomainModel(): Weather {
    return Weather(
        current = this.current.toCurrentWeather(),
        forecast = this.daily.toDailyForecastList()
    )
}