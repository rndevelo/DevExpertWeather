package com.rndeveloper.myapplication.framework.weather.local

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"])
data class DbCity(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double
)