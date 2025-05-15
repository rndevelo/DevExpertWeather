package com.rndeveloper.framework.core

import com.rndeveloper.myapplication.framework.location.remote.CityService
import com.rndeveloper.myapplication.framework.weather.remote.WeatherService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

internal object AppClient {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val weatherService: WeatherService = instance.create(WeatherService::class.java)
    val cityService: CityService = instance.create(CityService::class.java)
}