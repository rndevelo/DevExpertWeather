package com.rndeveloper.myapplication.data.datasource

import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.rndeveloper.myapplication.common.getFromLocationCompat
import com.rndeveloper.myapplication.data.datasource.remote.City

class RegionDataSource(
    app: Application,
    private val locationDataSource: LocationDataSource
) {

    private val geocoder = Geocoder(app)

    suspend fun findLastLocationCityInfo(): City =
        locationDataSource.findLastLocation()?.toCityInfo() ?: City()

    private suspend fun Location.toCityInfo(): City {
        val addresses = geocoder.getFromLocationCompat(latitude, longitude, 1)
        val locality = addresses.firstOrNull()?.locality ?: "Unknown locality"
        val country = addresses.firstOrNull()?.countryName ?: "Unknown country"
        return City().copy(
            name = locality,
            country = country,
            latitude = latitude,
            longitude = longitude
        )
    }
}