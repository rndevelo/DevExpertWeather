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
interface CitiesDao {
    @Query("SELECT * FROM City")
    fun getFavCities(): Flow<List<City>>

//    @Query("SELECT * FROM Weather")
//    fun getWeather(): Flow<Weather>

    @Query("SELECT * FROM City WHERE name = :name")
    fun getCityByName(name: String): Flow<City>

    @Query("SELECT COUNT(*) FROM City")
    suspend fun countCities(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)
}