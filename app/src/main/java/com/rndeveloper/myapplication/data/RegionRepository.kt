package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.datasource.RegionDataSource
import com.rndeveloper.myapplication.data.datasource.remote.City

class RegionRepository(private val regionDataSource: RegionDataSource) {

    suspend fun findLastLocationCityInfo(): City = regionDataSource.findLastLocationCityInfo()
}