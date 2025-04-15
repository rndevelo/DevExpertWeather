package com.rndeveloper.myapplication.location

import com.rndeveloper.myapplication.common.City


interface RegionDataSource {
    suspend fun findLastLocationCityInfo(): City?
}

