package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class CityRepositoryImpl @Inject constructor(
    private val cityLocalDataSource: CityLocalDataSource,
    private val cityRemoteDataSource: CityRemoteDataSource,
) : CityRepository {

    override val selectedCity: Flow<City?> = flow { emitAll(cityLocalDataSource.selectedCity) }

    override suspend fun insertSelectedCity(city: City) = cityLocalDataSource.insertSelectedCity(city)

    override val favCities: Flow<List<City>> = flow { emitAll(cityLocalDataSource.favCities) }

    override suspend fun searchCities(query: String) = cityRemoteDataSource.searchCities(query)

    override suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            cityLocalDataSource.deleteFavCity(city)
        } else {
            cityLocalDataSource.insertFavCity(city)
        }
    }
}