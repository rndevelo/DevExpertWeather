package com.rndeveloper.myapplication.framework.weather.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DbCity::class, DbWeather::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverters::class)
internal abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}