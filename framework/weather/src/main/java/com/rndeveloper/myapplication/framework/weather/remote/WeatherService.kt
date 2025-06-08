package com.rndeveloper.myapplication.framework.weather.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("forecast")
    suspend fun fetchWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,precipitation,windspeed_10m,weathercode",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum",
        @Query("timezone") timezone: String = "auto"
    ): RemoteWeather
}