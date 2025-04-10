package com.rndeveloper.myapplication.data.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.WeatherTypeConverters
import com.rndeveloper.myapplication.data.datasource.remote.City

@Database(entities = [City::class, Weather::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}