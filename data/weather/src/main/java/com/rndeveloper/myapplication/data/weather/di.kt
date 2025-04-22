package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataWeatherBindsModule {

    @Binds
    abstract fun bindLocationDataSource(impl: WeatherRepositoryImpl): WeatherRepository
}
