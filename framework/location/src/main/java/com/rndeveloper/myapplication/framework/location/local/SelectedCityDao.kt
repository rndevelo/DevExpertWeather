package com.rndeveloper.myapplication.framework.location.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SelectedCityDao {
    @Query("SELECT * FROM selected_city LIMIT 1") // Es buena práctica usar LIMIT 1 para un solo objeto
    fun getSelectedCity(): Flow<DbSelectedCity?> // HACER EL TIPO DE RETORNO NULABLE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedCity(city: DbSelectedCity)
}