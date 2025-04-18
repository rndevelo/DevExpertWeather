package com.rndeveloper.myapplication.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Forecast(val cityName: String, val lat: String, val long: String)