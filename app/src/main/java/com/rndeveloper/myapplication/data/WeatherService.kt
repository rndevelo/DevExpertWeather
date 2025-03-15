package com.rndeveloper.myapplication.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("current")
    suspend fun fetchWeather(@Query("query") location: String): RemoteResult
}