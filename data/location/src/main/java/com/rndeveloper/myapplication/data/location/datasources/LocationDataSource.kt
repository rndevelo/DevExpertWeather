package com.rndeveloper.myapplication.data.location.datasources

import com.rndeveloper.myapplication.domain.location.City

interface LocationDataSource {
    suspend fun findLastLocation(): City?
}

