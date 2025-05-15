package com.rndeveloper.myapplication.framework.weather.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM DbWeather WHERE lat = :lat AND lon = :lon")
    fun getWeather(lat: Double, lon: Double): Flow<DbWeather?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: DbWeather)
}