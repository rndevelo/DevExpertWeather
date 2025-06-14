package com.rndeveloper.myapplication.feature.home

import app.cash.turbine.test
import com.rndeveloper.myapplication.data.buildCityRepositoryWith
import com.rndeveloper.myapplication.data.buildRegionRepositoryWith
import com.rndeveloper.myapplication.data.buildWeatherRepositoryWith
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetFromLocationCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.ToggleCityUseCase
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    val cityData = sampleCity()
    val otherCityData = sampleCity(40.7128, -74.0060)
    val weatherData = sampleWeather()

    @Test
    fun `Weather data is loaded from server when local source is empty`() = runTest {

        val vm = buildViewModelWith(
            weatherLocalData = null,
            weatherRemoteData = weatherData,
            selectedCityLocalData = cityData
        )

        vm.onAction(HomeAction.OnSelectedCity(cityData))

        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
//            assertEquals(Result.Success(null), awaitItem())
            assertEquals(Result.Success(weatherData), awaitItem())
        }
    }

    @Test
    fun `Weather data is loaded from local source when available`() = runTest {
        val localWeatherData = sampleWeather()

        val vm = buildViewModelWith(
            weatherLocalData = localWeatherData,
            selectedCityLocalData = cityData
        )

        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localWeatherData), awaitItem())
        }
    }

    @Test
    fun `Selected city data is loaded from local source when available`() = runTest {
        val vm = buildViewModelWith(selectedCityLocalData = cityData)

        vm.selectedCityState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(cityData), awaitItem())
        }
    }

    @Test
    fun `Fav cities data is loaded from local source when available`() = runTest {
        val localFavCitiesData = mutableListOf(sampleCity())

        val vm = buildViewModelWith(favCitiesLocalData = localFavCitiesData)

        vm.favCitiesState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localFavCitiesData), awaitItem())
        }
    }

    @Test
    fun `Selected city is updated in local data source`() = runTest {

        val vm = buildViewModelWith(selectedCityLocalData = cityData)

        vm.selectedCityState.test {

            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(cityData), awaitItem())

            vm.onAction(HomeAction.OnSelectedCity(otherCityData))

            assertEquals(Result.Success(otherCityData), awaitItem())
        }
    }

    @Test
    fun `Favorite city is updated in local data source`() = runTest {

        val localFavCitiesData = listOf<City>(cityData)

        val vm = buildViewModelWith(favCitiesLocalData = localFavCitiesData)

        vm.favCitiesState.test {

            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localFavCitiesData), awaitItem())

            vm.onAction(HomeAction.OnToggleCity(otherCityData, false))

            assertEquals(Result.Success(listOf<City>(cityData, otherCityData)), awaitItem())
        }
    }

    @Test
    fun `Favorite city is deleted in local data source`() = runTest {

        val localFavCitiesData = listOf<City>(cityData, otherCityData)

        val vm = buildViewModelWith(favCitiesLocalData = localFavCitiesData)

        vm.favCitiesState.test {

            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localFavCitiesData), awaitItem())

            vm.onAction(HomeAction.OnToggleCity(otherCityData, true))

            assertEquals(Result.Success(listOf<City>(cityData)), awaitItem())
        }
    }

    @Test
    fun `Searched cities is called from remote data source`() = runTest {
        val emptySearchedCitiesData = emptyList<City>()
        val searchedCitiesRemoteData = listOf<City>(cityData, otherCityData)

        val vm = buildViewModelWith(searchedCitiesRemoteData = searchedCitiesRemoteData)

        vm.searchedCitiesState.test {

            assertEquals(emptySearchedCitiesData, awaitItem())

            vm.onAction(HomeAction.OnSearchCities("CityName"))

            assertEquals(searchedCitiesRemoteData, awaitItem())
        }
    }

    @Test
    fun `Init selected city is called from GPS remote data source`() = runTest {

        val vm = buildViewModelWith(
            cityGPSRemoteData = cityData,
            searchedCitiesRemoteData = listOf<City>(cityData, otherCityData)
        )

        vm.selectedCityState.test {
            assertEquals(Result.Loading, awaitItem())

            vm.onAction(HomeAction.OnGetCityFromGPSLocation)

            assertEquals(Result.Success(cityData), awaitItem())
        }
    }
}

private fun buildViewModelWith(
    weatherLocalData: Weather? = null,
    weatherRemoteData: Weather? = null,
    selectedCityLocalData: City? = null,
    favCitiesLocalData: List<City> = emptyList(),
    searchedCitiesRemoteData: List<City> = emptyList(),
    cityGPSRemoteData: City? = null,
): HomeViewModel {

    val cityRepository = buildCityRepositoryWith(
        selectedCityLocalData = selectedCityLocalData,
        favCitiesLocalData = favCitiesLocalData,
        searchedCityRemoteData = searchedCitiesRemoteData
    )

    val getWeatherUseCase =
        GetWeatherUseCase(buildWeatherRepositoryWith(weatherLocalData, weatherRemoteData))
    val getSelectedCity = GetSelectedCityUseCase(cityRepository)
    val setSelectedCity = SetSelectedCityUseCase(cityRepository)
    val getFavCities = GetFavCitiesUseCase(cityRepository)
    val toggleCity = ToggleCityUseCase(cityRepository)
    val searchCities = SearchCitiesUseCase(cityRepository)
    val getCityFromLocationCityUseCase =
        GetFromLocationCityUseCase(buildRegionRepositoryWith(cityGPSRemoteData = cityGPSRemoteData))

    return HomeViewModel(
        getWeatherUseCase,
        getSelectedCity,
        setSelectedCity,
        getFavCities,
        toggleCity,
        searchCities,
        getCityFromLocationCityUseCase
    )
}