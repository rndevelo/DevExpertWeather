package com.rndeveloper.myapplication.location

import com.rndeveloper.myapplication.common.City

interface RegionRepository {
    suspend fun findLastLocationCityInfo(): City?
}