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

    private val exceptedCity = sampleCity()

    private val exceptedWeather = sampleWeather()

    @Before
    fun setUp() {

        whenever(getFavCitiesUseCase()).thenReturn(flowOf(emptyList()))
        whenever(getSelectedCityUseCase()).thenReturn(flowOf(null))

        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            toggleCityUseCase,
            searchCitiesUseCase,
            getFromLocationCityUseCase
        )
    }

    /**
     * SelectedCity Test
     */
    @Test
    fun `Selected city is not requested from database yet`() = runTest {
        vm.state.first()

        runCurrent()

        verify(getSelectedCityUseCase).invoke()
    }

    @Test
    fun `Selected city is requested from database`() = runTest {
        val selectedCitySourceFlow = MutableStateFlow<City?>(null)

        whenever(getSelectedCityUseCase()).thenReturn(selectedCitySourceFlow)
        whenever(setSelectedCityUseCase.invoke(any())).thenAnswer { invocation ->
            val cityToSet = invocation.arguments.first() as City
            selectedCitySourceFlow.value = cityToSet
        }

        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            toggleCityUseCase,
            searchCitiesUseCase,
            getFromLocationCityUseCase
        )

        vm.selectedCityState.test {

            var currentState = awaitItem()

            println("Estado 1 (Post-Init): $currentState")
            assertEquals(Result.Loading, currentState)

            // --- ACCIÓN: Seleccionar una ciudad ---
            vm.onAction(HomeAction.OnSelectedCity(exceptedCity))

            currentState = awaitItem()
            // --- ESTADO 2: Ciudad seleccionada ---
            // selectedCityState ahora es Result.Success(selectedCityObject).
            println("Estado 2 (Ciudad Seleccionada): $currentState")
            assertEquals(Result.Success(exceptedCity), currentState)
        }
    }


    /**
     * Weather Test
     */
    @Test
    fun `Weather is not requested if selected city is not ready`() = runTest {
        vm.state.first()

        runCurrent()

        verify(getWeatherUseCase, times(0)).invoke(any(), any())
    }

    @Test
    fun `Weather is requested when selected city is ready`() = runTest {

        // 1. Configuración de Mocks
        whenever(getSelectedCityUseCase()).thenReturn(flowOf(exceptedCity)) // Devuelve Flow<City?>
        whenever(
            getWeatherUseCase(
                any(),
                any()
            )
        ).thenReturn(flowOf(exceptedWeather)) // Devuelve Flow<Weather>

        // 2. Reinicializa el ViewModel
        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            toggleCityUseCase,
            searchCitiesUseCase,
            getFromLocationCityUseCase
        )

        // 3. Assert: Verifica las emisiones del Flow 'state' con Turbine
        vm.weatherState.test {
            // --- ESTADO 1: Inicialización Completa del ViewModel ---
            // selectedCityState será Result.Loading porque selectedCitySourceFlow es null
            // y stateAsResultIn lo mapea.
            // weatherState será Result.Loading porque su fuente (vía mapNotNull) no ha emitido una ciudad válida.
            var currentState = awaitItem()

            println("Estado 1 (Post-Init): $currentState")
            assertEquals(Result.Loading, currentState)

            currentState = awaitItem()
            // --- ESTADO 3: Ciudad seleccionada, Clima cargado con éxito ---
            // Ahora getWeatherUseCase (flowOf(expectedWeatherObject)) debería haber emitido,
            // y el stateAsResultIn de weatherState lo mapea a Result.Success.
            println("Estado 3 (Clima Éxito): $currentState")
            assertEquals(Result.Success(exceptedWeather), currentState)
        }
    }

    @Test
    fun `Weather error is propagated when request fails`() = runTest {
        val exceptedError = RuntimeException("Boom!")

        whenever(getSelectedCityUseCase()).thenReturn(flowOf(exceptedCity)) // Devuelve Flow<City?>
        whenever(getWeatherUseCase(any(), any())).thenThrow(exceptedError)

        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            toggleCityUseCase,
            searchCitiesUseCase,
            getFromLocationCityUseCase
        )

        vm.weatherState.test {
            var currentState = awaitItem()
            println("Estado 1 (Post-Init): $currentState")
            assertEquals(Result.Loading, currentState)

            currentState = awaitItem()
            val exceptionMessage = (currentState as Result.Error).exception.message
            println("Estado 2 (Clima Error): $exceptionMessage")
            assertEquals("Boom!", exceptionMessage)
        }
    }


    /**
     * FavCities Test
     */
    @Test
    fun `Fav cities is requested when data base is called`() = runTest {

        val exceptedFavCities = emptyList<City>()

        vm.favCitiesState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(exceptedFavCities), awaitItem())
        }
    }


    /**
     * Actions Test
     */
    @Test
    fun `OnAction - OnGetCityFromLocation - return expected value`() = runTest {

        val expectedCity = sampleCity()
        val expectedSearchedCity = listOf<City>(sampleCity(), sampleCity())

        whenever(getFromLocationCityUseCase.invoke()).thenReturn(expectedCity)
        whenever(searchCitiesUseCase.invoke("${expectedCity.name}, ${expectedCity.country}")).thenReturn(
            expectedSearchedCity
        )

        vm.onAction(HomeAction.OnGetCityFromGPSLocation)

        runCurrent()

        verify(setSelectedCityUseCase).invoke(expectedSearchedCity.first())
    }

    @Test
    fun `OnAction - OnSearchCities - return expected value`() = runTest {

        val expected = emptyList<City>()

        whenever(searchCitiesUseCase.invoke(any())).thenReturn(expected)

        vm.onAction(HomeAction.OnSearchCities(""))

        assertEquals(expected, vm.searchedCitiesState.value)
    }

    @Test
    fun `OnAction - OnSelectedCity - excepted value is saved to database`() = runTest {

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
}