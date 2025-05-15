package com.rndeveloper.myapplication.domain.location

import kotlinx.coroutines.flow.Flow

interface CityRepository {
    val favCities: Flow<List<City>>
    suspend fun searchCities(query: String): List<City>
    suspend fun toggleFavCity(city: City, isFav: Boolean)
}