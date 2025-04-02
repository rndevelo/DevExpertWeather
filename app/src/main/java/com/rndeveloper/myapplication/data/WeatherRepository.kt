package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.datasource.CitiesInfoLocalDataSource
import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.data.datasource.remote.City
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class WeatherRepository(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: CitiesInfoLocalDataSource
) {

    suspend fun getWeather(lat: Double, lon: Double) = weatherRemoteDataSource.getWeather(lat, lon)

    suspend fun searchCities(query: String) = weatherRemoteDataSource.searchCities(query)

    val favCities: Flow<List<City>> = localDataSource.favCities.transform {
        val cities = it.takeIf { it.isNotEmpty() } ?: emptyList()
        emit(cities)
    }

    suspend fun toggleFavCity(city: City, isFav: Boolean) {
        if (isFav) {
            localDataSource.deleteCity(city)
        } else {
            localDataSource.insertCity(city)
        }
    }


    //    Función interesante para probar algo semejante en el futuro con una base de datos remota

//    val favCities: Flow<List<City>> = localDataSource.favCities.transform {
//        val cities = it.takeIf { it.isNotEmpty() }
//            ?: weatherRemoteDataSource.searchCities("query").also {
//                insertCity(it.first())
//            }
//        emit(cities)
//    }
}

