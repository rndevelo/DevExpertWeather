package com.rndeveloper.myapplication.framework.location.remote

import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.domain.location.City
import jakarta.inject.Inject

internal class WeatherServerDataSource @Inject constructor(private val cityService: CityService) : CityRemoteDataSource {

    override suspend fun searchCities(query: String): List<City> {
        return try {
            val response = cityService.searchCities(query)
            response.results.map { it.toCity() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}