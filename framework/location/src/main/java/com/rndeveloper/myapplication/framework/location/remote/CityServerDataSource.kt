package com.rndeveloper.myapplication.framework.location.remote

import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.domain.location.City
import jakarta.inject.Inject

internal class CityServerDataSource @Inject constructor(private val cityService: CityService) : CityRemoteDataSource {

    override suspend fun searchCities(query: String): List<City> =
        cityService.searchCities(query).results.map { it.toCity() }

}