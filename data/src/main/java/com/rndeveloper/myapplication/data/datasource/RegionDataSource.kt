package com.rndeveloper.myapplication.data.datasource

import com.rndeveloper.myapplication.domain.City

interface RegionDataSource {
    suspend fun findLastLocationCityInfo(): City?
}

