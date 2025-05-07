package com.rndeveloper.myapplication.framework.weather.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

//    Debería de tener 2 peticiones en distintas? Ya que este endpoint lo uso para 2 cosas distintas pero este mismo me provee las 2
    @GET("forecast")
    suspend fun fetchWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,precipitation,windspeed_10m,weathercode",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weathercode,precipitation_sum",
        @Query("timezone") timezone: String = "auto"
    ): RemoteWeather

    // 🔥 Nueva función para buscar ciudades
    @GET("https://geocoding-api.open-meteo.com/v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 5,
        @Query("language") language: String = "es",
        @Query("format") format: String = "json"
    ): GeoCodingResponse
}