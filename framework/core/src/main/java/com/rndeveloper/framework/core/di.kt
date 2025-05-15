package com.rndeveloper.framework.core

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object FrameworkWeatherModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, "app-db").build()

    @Provides
    @Singleton
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Provides
    @Singleton
    fun provideCityDao(db: AppDatabase) = db.cityDao()

    @Provides
    @Singleton
    fun provideWeatherService() = AppClient.weatherService

    @Provides
    @Singleton
    fun provideCityService() = AppClient.cityService
}

