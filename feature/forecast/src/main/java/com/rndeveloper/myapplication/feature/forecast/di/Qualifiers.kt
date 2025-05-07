package com.rndeveloper.myapplication.feature.forecast.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CityName

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Latitude

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Longitude