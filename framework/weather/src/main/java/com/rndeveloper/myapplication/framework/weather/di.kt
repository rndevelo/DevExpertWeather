package com.rndeveloper.myapplication.framework.weather

import com.rndeveloper.myapplication.data.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.framework.weather.local.WeatherRoomDataSource
import com.rndeveloper.myapplication.framework.weather.remote.WeatherServerDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
