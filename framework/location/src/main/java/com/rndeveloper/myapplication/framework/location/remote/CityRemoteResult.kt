package com.rndeveloper.myapplication.framework.location.remote

import kotlinx.serialization.Serializable
import com.rndeveloper.myapplication.domain.location.City

@Serializable
data class GeoCodingResponse(val results: List<RemoteCity>)

@Serializable
data class RemoteCity(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
){
    fun toCity(): City {
        return City(
            name = name,
            country = country,
            lat = latitude,
            lon = longitude
        )
    }
}