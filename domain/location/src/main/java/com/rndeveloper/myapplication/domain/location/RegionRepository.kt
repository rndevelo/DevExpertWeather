package com.rndeveloper.myapplication.domain.location

interface RegionRepository {
    suspend fun findLastLocationCityInfo(): City?
}