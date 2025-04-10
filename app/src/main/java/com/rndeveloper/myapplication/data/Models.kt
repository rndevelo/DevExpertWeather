package com.rndeveloper.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val current: Current,
    val forecast: List<DailyForecast>,
    val lastUpdated: Long, // tiempo en milisegundos
    val lat: Double,
    val lon: Double
)


data class Current(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitation: Double,
)


// Nueva clase para representar el pronóstico diario de forma individual
data class DailyForecast(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val precipitation: Double,
)

class WeatherTypeConverters {

    private val gson = Gson()

    // Convertir Current a JSON
    @TypeConverter
    fun fromCurrent(current: Current): String = gson.toJson(current)

    // Convertir JSON a Current
    @TypeConverter
    fun toCurrent(currentJson: String): Current =
        gson.fromJson(currentJson, Current::class.java)

    // Convertir lista de DailyForecast a JSON
    @TypeConverter
    fun fromDailyForecastList(forecasts: List<DailyForecast>): String = gson.toJson(forecasts)

    // Convertir JSON a lista de DailyForecast
    @TypeConverter
    fun toDailyForecastList(forecastsJson: String): List<DailyForecast> {
        val listType = object : TypeToken<List<DailyForecast>>() {}.type
        return gson.fromJson(forecastsJson, listType)
    }
}