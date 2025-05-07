package com.rndeveloper.myapplication.feature.common

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Forecast(val cityName: String, val lat: String, val lon: String)
