package com.rndeveloper.framework.core

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherApiUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeocodingApiUrl