package com.rndeveloper.myapplication.framework.location.local

import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.domain.location.City
import jakarta.inject.Inject
import kotlinx.coroutines.flow.map

internal class CityRoomDataSource @Inject constructor(
    private val favCityDao: FavCityDao,
    private val selectedCityDao: SelectedCityDao
) :
    CityLocalDataSource {

    override val selectedCity = selectedCityDao.getSelectedCity().map { dbCity -> dbCity?.toCity() }
    override suspend fun insertSelectedCity(city: City) =
        selectedCityDao.insertSelectedCity(city.toDbSelectedCity())

    override val favCities = favCityDao.getFavCities().map { dbCities -> dbCities.map { it.toCity() } }
    override suspend fun insertFavCity(city: City) = favCityDao.insertFavCity(city.toDbFavCity())
    override suspend fun deleteFavCity(city: City) = favCityDao.deleteFavCity(city.toDbFavCity())
}

private fun City.toDbFavCity(): DbFavCity {
    return DbFavCity(
        name = name,
        country = country,
        lat = lat,
        lon = lon,
    )
}

fun DbFavCity.toCity(): City {
    return City(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}

private fun City.toDbSelectedCity(): DbSelectedCity {
    return DbSelectedCity(
        name = name,
        country = country,
        lat = lat,
        lon = lon,
    )
}

fun DbSelectedCity.toCity(): City {
    return City(
        name = name,
        country = country,
        lat = lat,
        lon = lon
    )
}
