package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.location.RegionRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataLocationModule = module {
    factoryOf(::RegionRepositoryImpl) bind RegionRepository::class
}
