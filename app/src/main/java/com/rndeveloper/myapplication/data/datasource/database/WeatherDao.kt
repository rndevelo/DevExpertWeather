package com.rndeveloper.myapplication.data.datasource.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.datasource.remote.City
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM City")
    fun getFavCities(): Flow<List<City>>

    @Query("SELECT * FROM Weather WHERE lat = :lat AND lon = :lon")
    fun getWeather(lat: Double, lon: Double): Flow<Weather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: Weather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCity(city: City)

    @Delete
    suspend fun deleteFavCity(city: City)
}