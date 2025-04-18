package com.rndeveloper.myapplication.location

import com.rndeveloper.myapplication.common.City


class RegionRepositoryImpl(private val regionDataSource: RegionDataSource) : RegionRepository {
    override suspend fun findLastLocationCityInfo(): City? = regionDataSource.findLastLocationCityInfo()
}
