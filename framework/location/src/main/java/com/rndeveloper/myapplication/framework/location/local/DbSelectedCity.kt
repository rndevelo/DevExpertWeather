package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_city")
data class DbSelectedCity(
    @PrimaryKey val id: Int = 0,  // Solo un registro fijo
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val lastUpdated: Long = System.currentTimeMillis() // 👈 nueva columna
)