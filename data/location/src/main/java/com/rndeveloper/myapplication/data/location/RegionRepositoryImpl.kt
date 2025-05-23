package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.data.location.datasources.RegionDataSource
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.RegionRepository
import jakarta.inject.Inject

class RegionRepositoryImpl @Inject constructor(private val regionDataSource: RegionDataSource) :
    RegionRepository {
    override suspend fun cityByLastLocation(): City? =
        regionDataSource.findLastLocationCityInfo()
}
