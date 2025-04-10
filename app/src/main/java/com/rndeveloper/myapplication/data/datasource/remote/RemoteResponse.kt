package com.rndeveloper.myapplication.data.datasource.remote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rndeveloper.myapplication.common.formatUiDate
import com.rndeveloper.myapplication.common.getWeatherDescription
import com.rndeveloper.myapplication.common.getWeatherIcon
import com.rndeveloper.myapplication.data.Current
import com.rndeveloper.myapplication.data.DailyForecast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteWeather(
    val current: RemoteCurrentWeather,
    val daily: RemoteDailyForecast
)

@Serializable
data class RemoteCurrentWeather(
    @SerialName(value = "time") val date: String,
    @SerialName(value = "weathercode") val weatherCode: Int,
    @SerialName(value = "temperature_2m") val temperature: Double,
    @SerialName(value = "relative_humidity_2m") val humidity: Int,
    @SerialName(value = "windspeed_10m") val windSpeed: Double,
    @SerialName(value = "precipitation") val precipitation: Double,
) {
    // Convertimos los datos en un objeto CurrentWeather para nuestro dominio
    fun toCurrentWeather(): Current {
        return Current(
            date = date.formatUiDate(),
            weatherDescription = weatherCode.getWeatherDescription(),
            weatherIcon = weatherCode.getWeatherIcon(),
            temperature = temperature,
            humidity = humidity,
            windSpeed = windSpeed,
            precipitation = precipitation,
        )
    }
}

@Serializable
data class RemoteDailyForecast(
    @SerialName(value = "time") val dates: List<String>,
    @SerialName(value = "temperature_2m_max") val maxTemperatures: List<Double>,
    @SerialName(value = "temperature_2m_min") val minTemperatures: List<Double>,
    @SerialName(value = "precipitation_sum") val precipitationSum: List<Double>,
    @SerialName(value = "weathercode") val weatherCodes: List<Int>
) {
    // Convertimos los datos en una lista de objetos DailyForecast para nuestro dominio
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDailyForecastList(): List<DailyForecast> {
        return dates.mapIndexed { index, date ->
            DailyForecast(
                date = date.formatUiDate(),
                weatherDescription = weatherCodes.getOrNull(index)!!.getWeatherDescription(),
                weatherIcon = weatherCodes.getOrNull(index)!!.getWeatherIcon(),
                maxTemperature = maxTemperatures.getOrNull(index) ?: 0.0,
                minTemperature = minTemperatures.getOrNull(index) ?: 0.0,
                precipitation = precipitationSum.getOrNull(index) ?: 0.0,
            )
        }
    }
}

@Serializable
data class GeoCodingResponse(val results: List<City>)

@Entity
@Serializable
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
){
    constructor() : this(
        id = 0,
        name = "",
        country = "",
        latitude = 40.71,
        longitude = 0.0,
    )
}