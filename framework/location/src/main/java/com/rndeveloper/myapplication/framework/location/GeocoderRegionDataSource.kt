package com.rndeveloper.myapplication.framework.location

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.rndeveloper.myapplication.data.location.LocationDataSource
import com.rndeveloper.myapplication.data.location.RegionDataSource
import com.rndeveloper.myapplication.domain.common.City
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class GeocoderRegionDataSource @Inject constructor(
    private val geocoder: Geocoder,
    private val locationDataSource: LocationDataSource
) : RegionDataSource {

    override suspend fun findLastLocationCityInfo(): City? =
        locationDataSource.findLastLocation()?.toCityInfo()

    private suspend fun City.toCityInfo(): City {
        val addresses = geocoder.getFromLocationCompat(lat, lon, 1)
        val locality = addresses.firstOrNull()?.locality ?: "Unknown locality"
        val country = addresses.firstOrNull()?.countryName ?: "Unknown country"
        return City(
            name = locality,
            country = country,
            lat = lat,
            lon = lon
        )
    }
}

@Suppress("DEPRECATION")
suspend fun Geocoder.getFromLocationCompat(
    @FloatRange(from = -90.0, to = 90.0) latitude: Double,
    @FloatRange(from = -180.0, to = 180.0) longitude: Double,
    @IntRange maxResults: Int
): List<Address> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    suspendCancellableCoroutine { continuation ->
        getFromLocation(latitude, longitude, maxResults) {
            continuation.resume(it)
        }
    }
} else {
    withContext(Dispatchers.IO) {
        getFromLocation(latitude, longitude, maxResults) ?: emptyList()
    }
}