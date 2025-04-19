package com.rndeveloper.myapplication.domain.location

import com.rndeveloper.myapplication.domain.common.City

interface RegionRepository {
    suspend fun findLastLocationCityInfo(): City?
}