package com.rndeveloper.myapplication

import android.app.Application
import androidx.room.Room
import com.rndeveloper.myapplication.data.datasource.database.WeatherDatabase

class App : Application() {

    lateinit var db: WeatherDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            WeatherDatabase::class.java,
            "cities_info_database"
        ).build()
    }
}