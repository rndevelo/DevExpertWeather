package com.rndeveloper.myapplication.domain.location

import kotlinx.coroutines.flow.Flow

interface CityRepository {
    val selectedCity: Flow<City?>
    suspend fun insertSelectedCity(city: City)
    val favCities: Flow<List<City>>
    suspend fun searchCities(query: String): List<City>
    suspend fun toggleFavCity(city: City, isFav: Boolean)
}