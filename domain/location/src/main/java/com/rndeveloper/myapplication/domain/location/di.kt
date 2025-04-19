package com.rndeveloper.myapplication.domain.location

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainLocationModule = module {
    factoryOf(::GetLocationCityUseCase)
}