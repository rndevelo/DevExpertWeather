package com.rndeveloper.myapplication.data.location.datasources

import com.rndeveloper.myapplication.domain.location.City

interface CityRemoteDataSource {
    suspend fun searchCities(query: String): List<City>
}