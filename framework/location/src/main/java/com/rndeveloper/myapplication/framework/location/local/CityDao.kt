package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavCityDao {
    @Query("SELECT * FROM fav_cities")
    fun getFavCities(): Flow<List<DbFavCity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavCity(city: DbFavCity)

    @Delete
    suspend fun deleteFavCity(city: DbFavCity)
}

@Dao
interface SelectedCityDao {
    @Query("SELECT * FROM selected_city WHERE id = 0")
    fun getSelectedCity(): Flow<DbSelectedCity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedCity(city: DbSelectedCity)
}
