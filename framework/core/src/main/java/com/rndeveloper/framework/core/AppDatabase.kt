package com.rndeveloper.framework.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rndeveloper.myapplication.framework.location.local.CityDao
import com.rndeveloper.myapplication.framework.location.local.DbCity
import com.rndeveloper.myapplication.framework.weather.local.DbWeather
import com.rndeveloper.myapplication.framework.weather.local.WeatherDao
import com.rndeveloper.myapplication.framework.weather.local.WeatherTypeConverters

@Database(entities = [DbCity::class, DbWeather::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverters::class)
internal abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}
