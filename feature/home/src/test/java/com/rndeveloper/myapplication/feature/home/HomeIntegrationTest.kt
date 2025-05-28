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
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.rndeveloper.myapplication.feature.common.Result

class HomeIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    val selectedCityLocalData = sampleCity()

    @Test
    fun `data is loaded from server when local source is empty`() = runTest {
        val remoteWeatherData = sampleWeather()

        val vm = buildViewModelWith(
            weatherLocalData = null,
            weatherRemoteData = remoteWeatherData,
            selectedCityLocalData = selectedCityLocalData
        )

        vm.onAction(HomeAction.OnSelectedCity(selectedCityLocalData))

        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
//            assertEquals(Result.Success(null), awaitItem())
            assertEquals(Result.Success(remoteWeatherData), awaitItem())
        }
    }

    @Test
    fun `data is loaded from local source when available`() = runTest {
        val localWeatherData = sampleWeather()
        val vm = buildViewModelWith(
            weatherLocalData = localWeatherData,
            selectedCityLocalData = selectedCityLocalData
        )

        vm.onAction(HomeAction.OnSelectedCity(sampleCity()))

        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(localWeatherData), awaitItem())
        }
    }
}

private fun buildViewModelWith(
    weatherLocalData: Weather? = null,
    weatherRemoteData: Weather? = null,
    selectedCityLocalData: City? = null,
    favCitiesLocalData: MutableList<City> = mutableListOf<City>(),
    searchedCityRemoteData: List<City> = emptyList(),
    cityGPSRemoteData: City? = null,
): HomeViewModel {
    val getWeatherUseCase =
        GetWeatherUseCase(buildWeatherRepositoryWith(weatherLocalData, weatherRemoteData))
    val getSelectedCity =
        GetSelectedCityUseCase(buildCityRepositoryWith(selectedCityLocalData = selectedCityLocalData))
    val setSelectedCity =
        SetSelectedCityUseCase(buildCityRepositoryWith(selectedCityLocalData = selectedCityLocalData))
    val getFavCities =
        GetFavCitiesUseCase(buildCityRepositoryWith(favCitiesLocalData = favCitiesLocalData))
    val toggleCity =
        ToggleCityUseCase(buildCityRepositoryWith(favCitiesLocalData = favCitiesLocalData))
    val searchCities =
        SearchCitiesUseCase(buildCityRepositoryWith(searchedCityRemoteData = searchedCityRemoteData))
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