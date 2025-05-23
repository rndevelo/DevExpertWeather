package com.rndeveloper.myapplication.domain

import com.rndeveloper.myapplication.domain.location.City

fun sampleCity(lat: Double = -34.6037, lon: Double = -58.3816) = City(
    name = "City name",
    country = "Country",
    lat = lat,
    lon = lon
)
