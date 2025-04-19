package com.rndeveloper.myapplication.framework.weather

import androidx.room.Room
import com.rndeveloper.myapplication.framework.weather.local.WeatherDatabase
import com.rndeveloper.myapplication.framework.weather.remote.WeatherClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan
class FrameworkWeatherModule

val frameworkWeatherModule = module {
    single { Room.databaseBuilder(get(), WeatherDatabase::class.java, "weather-db").build() }
    factory { get<WeatherDatabase>().weatherDao() }
    single { WeatherClient.instance }
}