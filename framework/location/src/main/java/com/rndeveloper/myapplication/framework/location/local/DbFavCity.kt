package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Entity

@Entity(primaryKeys = ["lat", "lon"], tableName = "fav_cities")
data class DbFavCity(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)
