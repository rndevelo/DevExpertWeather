package com.rndeveloper.myapplication.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rndeveloper.myapplication.data.datasource.remote.City

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CitiesDatabase: RoomDatabase() {
    abstract fun citiesDao(): CitiesDao
}