package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Query("SELECT * FROM DbCity")
    fun getFavCities(): Flow<List<DbCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCity(dbCity: DbCity)

    @Delete
    suspend fun deleteFavCity(dbCity: DbCity)
}