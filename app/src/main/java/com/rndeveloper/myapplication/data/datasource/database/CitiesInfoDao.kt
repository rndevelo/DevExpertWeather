package com.rndeveloper.myapplication.data.datasource.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rndeveloper.myapplication.data.datasource.remote.City

@Dao
interface CitiesInfoDao {
    @Query("SELECT * FROM City")
    suspend fun getAllCities(): List<City>

    @Query("SELECT * FROM City WHERE name = :name")
    suspend fun getCityByName(name: String): City

    @Query("SELECT COUNT(*) FROM City")
    suspend fun countCities(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)
}