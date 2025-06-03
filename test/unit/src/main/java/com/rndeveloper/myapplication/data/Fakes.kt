package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.data.location.CityRepositoryImpl
import com.rndeveloper.myapplication.data.location.RegionRepositoryImpl
import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.data.location.datasources.RegionDataSource
import com.rndeveloper.myapplication.data.weather.WeatherLocalDataSource
import com.rndeveloper.myapplication.data.weather.WeatherRemoteDataSource
import com.rndeveloper.myapplication.data.weather.WeatherRepositoryImpl
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.CityRepository
import com.rndeveloper.myapplication.domain.location.RegionRepository
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import com.rndeveloper.myapplication.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// FAKE Weather Repository

fun buildWeatherRepositoryWith(
    weatherLocalData: Weather? = null,
    weatherRemoteData: Weather? = null
): WeatherRepository {
    val localDataSource =
        FakeWeatherLocalDataSource().apply { inMemoryWeather.value = weatherLocalData }
    val remoteDataSource = FakeWeatherRemoteDataSource().apply {
        if (weatherRemoteData != null) {
            weather = weatherRemoteData
        }
    }
    return WeatherRepositoryImpl(localDataSource, remoteDataSource)
}

class FakeWeatherLocalDataSource : WeatherLocalDataSource {

    val inMemoryWeather = MutableStateFlow<Weather?>(null)

    override fun weather(lat: Double, lon: Double): Flow<Weather?> = inMemoryWeather

    override suspend fun insertWeather(weather: Weather) {
        inMemoryWeather.value = weather
    }
}

class FakeWeatherRemoteDataSource : WeatherRemoteDataSource {

    var weather = sampleWeather()

    override suspend fun getWeather(lat: Double, lon: Double): Weather = weather
}


// FAKE City Repository

fun buildCityRepositoryWith(
    selectedCityLocalData: City? = null,
    favCitiesLocalData: List<City> = mutableListOf<City>(),
    searchedCityRemoteData: List<City> = emptyList()
): CityRepository {
    val fakeCityLocalDataSource =
        FakeCityLocalDataSource().apply {
            inMemorySelectedCity.value = selectedCityLocalData
            inMemoryFavCities.value = favCitiesLocalData
        }
    val fakeCityRemoteDataSource =
        FakeCityRemoteDataSource().apply { inMemorySearchedCities = searchedCityRemoteData }
    return CityRepositoryImpl(fakeCityLocalDataSource, fakeCityRemoteDataSource)
}

class FakeCityLocalDataSource : CityLocalDataSource {

    val inMemorySelectedCity = MutableStateFlow<City?>(null)
    val inMemoryFavCities = MutableStateFlow<List<City>>(emptyList())


    override val selectedCity: Flow<City?> = inMemorySelectedCity

    override val favCities: Flow<List<City>> = inMemoryFavCities

    override suspend fun insertSelectedCity(city: City) {
        inMemorySelectedCity.value = city
    }

    override suspend fun insertFavCity(city: City) {
        val current = inMemoryFavCities.value
        inMemoryFavCities.value = current.toMutableList().apply { add(city) }
    }

    override suspend fun deleteFavCity(city: City) {
        val current = inMemoryFavCities.value
        inMemoryFavCities.value = current.toMutableList().apply { remove(city) }
    }
}

class FakeCityRemoteDataSource : CityRemoteDataSource {

    var inMemorySearchedCities = emptyList<City>()

    override suspend fun searchCities(query: String): List<City> = inMemorySearchedCities
}


// FAKE Region Repository

fun buildRegionRepositoryWith(
    cityGPSRemoteData: City? = null,
): RegionRepository {
    val regionRemoteDataSource =
        FakeRegionDataSource().apply { inMemoryCity.value = cityGPSRemoteData }
    return RegionRepositoryImpl(regionRemoteDataSource)
}

class FakeRegionDataSource : RegionDataSource {

    val inMemoryCity = MutableStateFlow<City?>(null)

    override suspend fun findLastLocationCityInfo(): City? = inMemoryCity.value
}
