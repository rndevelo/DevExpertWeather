package com.rndeveloper.myapplication.weather.local

import androidx.room.Dao
import androidx.room.Delete
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


    @Query("SELECT * FROM DbCity")
    fun getFavCities(): Flow<List<DbCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCity(dbCity: DbCity)

    @Delete
    suspend fun deleteFavCity(dbCity: DbCity)
}