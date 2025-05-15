package com.rndeveloper.myapplication.framework.location.local

import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.domain.location.City
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

internal class CityRoomDataSource @Inject constructor(private val cityDao: CityDao) :
    CityLocalDataSource {

    override val favCities = cityDao.getFavCities().map { cities -> cities.map { it.toCity() } }
    override suspend fun insertFavCity(city: City) = cityDao.insertFavCity(city.toDbModel())
    override suspend fun deleteFavCity(city: City) = cityDao.deleteFavCity(city.toDbModel())
}

private fun City.toDbModel(): DbCity {
    return DbCity(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}

fun DbCity.toCity(): City {
    return City(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}
