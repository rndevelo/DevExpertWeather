package com.rndeveloper.myapplication.framework.weather.local

import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rndeveloper.myapplication.domain.weather.model.Current
import com.rndeveloper.myapplication.domain.weather.model.DailyForecast

@Entity(primaryKeys = ["lat", "lon"])
data class DbWeather(
    val current: Current,
    val forecast: List<DailyForecast>,
    val lastUpdated: Long, // tiempo en milisegundos
    val lat: Double,
    val lon: Double
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