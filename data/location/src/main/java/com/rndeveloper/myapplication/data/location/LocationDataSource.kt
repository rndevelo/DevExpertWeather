package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.common.City

interface LocationDataSource {
    suspend fun findLastLocation(): City?
}

