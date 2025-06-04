package com.rndeveloper.myapplication.domain

import com.rndeveloper.myapplication.domain.location.City

fun sampleCity(
    lat: Double = -34.6037,
    lon: Double = -58.3816,
    name: String = "Buenos Aires",
    country: String = "Argentina",
) = City(
    name = name,
    country = country,
    lat = lat,
    lon = lon
)
