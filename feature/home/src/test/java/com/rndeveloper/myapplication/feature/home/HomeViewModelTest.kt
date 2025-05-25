package com.rndeveloper.myapplication.feature.home

import app.cash.turbine.test
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetFromLocationCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.ToggleCityUseCase
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.collections.first

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getWeatherUseCase: GetWeatherUseCase

    @Mock
    lateinit var getSelectedCityUseCase: GetSelectedCityUseCase

    @Mock
    lateinit var setSelectedCityUseCase: SetSelectedCityUseCase

    @Mock
    lateinit var getFavCitiesUseCase: GetFavCitiesUseCase

    @Mock
    lateinit var searchCitiesUseCase: SearchCitiesUseCase

    @Mock
    lateinit var toggleCityUseCase: ToggleCityUseCase

    @Mock
    lateinit var getFromLocationCityUseCase: GetFromLocationCityUseCase

    private lateinit var vm: HomeViewModel

    @Before
    fun setUp() {

        whenever(getFavCitiesUseCase()).thenReturn(flowOf(emptyList()))
        whenever(getSelectedCityUseCase()).thenReturn(flowOf(null))

        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            searchCitiesUseCase,
            toggleCityUseCase,
            getFromLocationCityUseCase
        )
    }

    @Test
    fun `Selected city is not requested from database yet`() = runTest {
        vm.state.first()

        runCurrent()

        verify(getSelectedCityUseCase).invoke()
    }

    @Test
    fun `Weather is not requested if selected city is not ready`() = runTest {
        vm.state.first()

        runCurrent()

        verify(getWeatherUseCase, times(0)).invoke(any(), any())
    }

    @Test
    fun `Weather is requested if selected city is ready`() = runTest {
        val expectedWeather = sampleWeather()
        val selectedCity = sampleCity()

        val selectedCityFlow = MutableStateFlow<City?>(null)
        whenever(getSelectedCityUseCase()).thenReturn(selectedCityFlow)
        whenever(getWeatherUseCase(any(), any())).thenReturn(flowOf(expectedWeather))

        whenever(setSelectedCityUseCase.invoke(any())).thenAnswer {
            selectedCityFlow.value = it.arguments.first() as City
        }

        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            searchCitiesUseCase,
            toggleCityUseCase,
            getFromLocationCityUseCase
        )

        vm.onAction(HomeAction.OnGetCityFromLocation)

        vm.state.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(expectedWeather), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `OnAction - OnGetCityFromLocation - return expected value`() = runTest {

        val expectedCity = sampleCity()
        val expectedSearchedCity = listOf<City>(sampleCity(), sampleCity())

        whenever(getFromLocationCityUseCase.invoke()).thenReturn(expectedCity)
        whenever(searchCitiesUseCase.invoke("${expectedCity.name}, ${expectedCity.country}")).thenReturn(
            expectedSearchedCity
        )

        vm.onAction(HomeAction.OnGetCityFromLocation)

        runCurrent()

        verify(setSelectedCityUseCase).invoke(expectedSearchedCity.first())
    }

    @Test
    fun `OnAction - OnSearchCities - return expected value`() = runTest {

        val expected = emptyList<City>()

        whenever(searchCitiesUseCase.invoke(any())).thenReturn(expected)

        vm.onAction(HomeAction.OnSearchCities(""))

//        runCurrent()

        assertEquals(expected, vm.searchedCitiesState.value)
    }

    @Test
    fun `OnAction - OnSelectedCity - return expected value`() = runTest {

        val expected = sampleCity()

        vm.onAction(HomeAction.OnSelectedCity(expected))

        runCurrent()

        verify(setSelectedCityUseCase).invoke(expected)
    }

    @Test
    fun `OnAction - OnToggleCity - excepted value is saved to database`() = runTest {
        val expectedCity = sampleCity()
        val expectedIsFav = false

        vm.onAction(HomeAction.OnToggleCity(expectedCity, expectedIsFav))

        runCurrent()

        verify(toggleCityUseCase).invoke(expectedCity, expectedIsFav)
    }

    @Test
    fun `OnAction - OnToggleCity - excepted value is deleted to database`() = runTest {
        val expectedCity = sampleCity()
        val expectedIsFav = true

        vm.onAction(HomeAction.OnToggleCity(expectedCity, expectedIsFav))

        runCurrent()

        verify(toggleCityUseCase).invoke(expectedCity, expectedIsFav)
    }

    @Test
    fun `SearchedCities is called from api server`() = runTest {
        val searchedCities = emptyList<City>()

        whenever(searchCitiesUseCase.invoke(any())).thenReturn(searchedCities)

        vm.onAction(HomeAction.OnSearchCities(""))

        verify(searchCitiesUseCase).invoke(any())
    }
}