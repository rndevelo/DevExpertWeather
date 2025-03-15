package com.rndeveloper.myapplication.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteResult(
    val location: RemoteLocation,
    val current: RemoteCurrentWeather
)



@Serializable
data class RemoteLocation(
    val name: String,
    val country: String,
    @SerialName("localtime") val localTime: String
)

@Serializable
data class RemoteCurrentWeather(
    val temperature: Double,
    val humidity: Int,
    @SerialName("wind_speed") val windSpeed: Double,
    @SerialName("weather_icons") val weatherIcons: List<String>,
    @SerialName("weather_descriptions") val weatherDescriptions: List<String>,
)
