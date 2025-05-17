package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(primaryKeys = ["lat", "lon"])
//data class DbCity(
//    val name: String,
//    val country: String,
//    val lat: Double,
//    val lon: Double,
//    val isSelected: Boolean = false
//)

@Entity(primaryKeys = ["lat", "lon"], tableName = "fav_cities")
data class DbFavCity(
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)

@Entity(tableName = "selected_city")
data class DbSelectedCity(
    @PrimaryKey val id: Int = 0,  // Solo un registro fijo
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)