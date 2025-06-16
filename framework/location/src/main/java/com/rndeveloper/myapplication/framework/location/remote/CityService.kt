package com.rndeveloper.myapplication.framework.location.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {

    @GET("search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 5,
        @Query("language") language: String = "es",
        @Query("format") format: String = "json"
    ): GeoCodingResponse
}
