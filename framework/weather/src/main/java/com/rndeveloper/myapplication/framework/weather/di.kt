package com.rndeveloper.myapplication.framework.weather

import androidx.room.Room
import com.rndeveloper.myapplication.data.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.framework.weather.local.WeatherDatabase
import com.rndeveloper.myapplication.framework.weather.local.WeatherRoomDataSource
import com.rndeveloper.myapplication.framework.weather.remote.WeatherClient
import com.rndeveloper.myapplication.framework.weather.remote.WeatherServerDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val frameworkWeatherModule = module {
    single { Room.databaseBuilder(get(), WeatherDatabase::class.java, "weather-db").build() }
    factory { get<WeatherDatabase>().weatherDao() }
    single { WeatherClient.instance }
    factoryOf(::WeatherRoomDataSource) bind WeatherLocalDataSource::class
    factoryOf(::WeatherServerDataSource) bind WeatherRemoteDataSource::class
}