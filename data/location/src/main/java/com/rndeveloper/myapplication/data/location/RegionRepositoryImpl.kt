package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.location.RegionRepository
import org.koin.core.annotation.Factory

@Factory
class RegionRepositoryImpl(private val regionDataSource: RegionDataSource) : RegionRepository {
    override suspend fun findLastLocationCityInfo(): City? = regionDataSource.findLastLocationCityInfo()
}
