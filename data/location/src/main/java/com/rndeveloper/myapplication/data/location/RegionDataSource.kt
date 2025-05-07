package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.common.City


interface RegionDataSource {
    suspend fun findLastLocationCityInfo(): City?
}

