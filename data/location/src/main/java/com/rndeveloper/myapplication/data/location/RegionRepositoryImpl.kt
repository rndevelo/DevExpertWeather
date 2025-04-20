package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.location.RegionRepository
import jakarta.inject.Inject

class RegionRepositoryImpl @Inject constructor(private val regionDataSource: RegionDataSource) :
    RegionRepository {
    override suspend fun findLastLocationCityInfo(): City? =
        regionDataSource.findLastLocationCityInfo()
}
