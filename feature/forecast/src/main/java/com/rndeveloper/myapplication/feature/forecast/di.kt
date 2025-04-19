package com.rndeveloper.myapplication.feature.forecast

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val featureForecastModule = module {
    viewModelOf(::ForecastViewModel)
}