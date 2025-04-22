package com.rndeveloper.myapplication.framework.weather

import android.app.Application
import androidx.room.Room
import com.rndeveloper.myapplication.data.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.framework.weather.local.WeatherDatabase
import com.rndeveloper.myapplication.framework.weather.local.WeatherRoomDataSource
import com.rndeveloper.myapplication.framework.weather.remote.WeatherClient
import com.rndeveloper.myapplication.framework.weather.remote.WeatherServerDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FrameworkWeatherBindsModule {
    @Binds
    abstract fun bindWeatherLocalDataSource(
        weatherLocalDataSource: WeatherRoomDataSource
    ): WeatherLocalDataSource
    @Binds
    abstract fun bindWeatherRemoteDataSource(
        weatherRemoteDataSource: WeatherServerDataSource
    ): WeatherRemoteDataSource
}

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
    fun provideWeatherClient() = WeatherClient.instance
}

