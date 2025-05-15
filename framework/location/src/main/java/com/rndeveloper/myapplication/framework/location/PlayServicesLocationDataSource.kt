package com.rndeveloper.myapplication.framework.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.rndeveloper.myapplication.data.location.datasources.LocationDataSource
import com.rndeveloper.myapplication.domain.location.City
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PlayServicesLocationDataSource @Inject constructor(private val fusedLocationClient: FusedLocationProviderClient) :
    LocationDataSource {

    override suspend fun findLastLocation(): City? = fusedLocationClient.lastLocation()
}

@SuppressLint("MissingPermission")
private suspend fun FusedLocationProviderClient.lastLocation(): City? {
    return suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location.toDomainCity())
        }.addOnFailureListener {
            continuation.resume(null)
        }
    }
}

private fun Location.toDomainCity(): City = City("", "", latitude, longitude)