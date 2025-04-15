package com.rndeveloper.myapplication.location

import com.rndeveloper.myapplication.common.City

interface LocationDataSource {
    suspend fun findLastLocation(): City?
}

