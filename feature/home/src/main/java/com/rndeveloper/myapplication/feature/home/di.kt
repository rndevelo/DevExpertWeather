package com.rndeveloper.myapplication.feature.home

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureHomeModule = module {
    viewModelOf(::HomeViewModel)
}