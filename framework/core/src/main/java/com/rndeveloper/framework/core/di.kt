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
        Room.databaseBuilder(app, WeatherDatabase::class.java, "weather-db").build()

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherDatabase) = db.weatherDao()

    @Provides
    @Singleton
    fun provideCityDao(db: WeatherDatabase) = db.cityDao()

    @Provides
    @Singleton
    fun provideWeatherService() = WeatherClient.weatherService

    @Provides
    @Singleton
    fun provideCityService() = WeatherClient.cityService
}

