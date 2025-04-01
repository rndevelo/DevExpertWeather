package com.rndeveloper.myapplication

import android.app.Application
import androidx.room.Room
import com.rndeveloper.myapplication.data.datasource.database.CitiesDatabase

class App : Application() {

    lateinit var db: CitiesDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            this,
            CitiesDatabase::class.java,
            "cities_info_database"
        ).build()
    }
}