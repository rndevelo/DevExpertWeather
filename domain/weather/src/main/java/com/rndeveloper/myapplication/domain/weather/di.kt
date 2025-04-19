package com.rndeveloper.myapplication.domain.weather

import com.rndeveloper.myapplication.domain.weather.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.domain.weather.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.weather.usecases.ToggleCityUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainWeatherModule = module {
    factoryOf(::GetFavCitiesUseCase)
    factoryOf(::GetWeatherUseCase)
    factoryOf(::SearchCitiesUseCase)
    factoryOf(::ToggleCityUseCase)
}