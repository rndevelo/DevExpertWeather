package com.rndeveloper.myapplication.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

const val DEFAULT_REGION = "Madrid"

suspend fun Context.getRegion(): String {

    val geocoder = Geocoder(this)
    val addresses = this.getLocation()?.let {
        geocoder.getFromLocationCompat(it.latitude, it.longitude, 1)
    }

    val region = addresses?.firstOrNull()?.locality
    return region ?: DEFAULT_REGION
}

suspend fun Context.getLocation(): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    return fusedLocationClient.lastLocation()
}

@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.lastLocation(): Location? {
    return suspendCancellableCoroutine { continuation ->
        lastLocation.addOnSuccessListener { location ->
            continuation.resume(location)
        }.addOnFailureListener {
            continuation.resume(null)
        }
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
    }} else {
    withContext(Dispatchers.IO) {
        getFromLocation(latitude, longitude, maxResults) ?: emptyList()
    }
}