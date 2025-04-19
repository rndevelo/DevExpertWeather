package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataWeatherModule = module {
    factoryOf(::WeatherRepositoryImpl) bind WeatherRepository::class
}
