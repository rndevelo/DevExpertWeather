package com.rndeveloper.myapplication.data.location.datasources

import com.rndeveloper.myapplication.domain.location.City
import kotlinx.coroutines.flow.Flow

interface CityLocalDataSource {
    val favCities: Flow<List<City>>
    suspend fun insertFavCity(city: City)
    suspend fun deleteFavCity(city: City)
}