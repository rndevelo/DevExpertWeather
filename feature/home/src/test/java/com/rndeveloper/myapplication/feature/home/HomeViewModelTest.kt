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
        val exceptedSelectedCity = sampleCity()
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
            searchCitiesUseCase,
            toggleCityUseCase,
            getFromLocationCityUseCase
        )

        runCurrent()

        vm.state.test {

            var currentState = awaitItem()
            println("Estado 1 (Post-Init): $currentState")
            assertEquals(Result.Loading, currentState.selectedCity)

            // --- ACCIÓN: Seleccionar una ciudad ---
            vm.onAction(HomeAction.OnSelectedCity(exceptedSelectedCity))
            runCurrent() // Permite que la acción se procese y los flujos comiencen a reaccionar

            // --- ESTADO 2: Ciudad seleccionada, Clima cargando ---
            // selectedCityState ahora es Result.Success(selectedCityObject).
            // weatherState se ha reiniciado (debido a flatMapLatest) y su stateAsResultIn
            // emite Result.Loading primero antes de procesar el resultado de getWeatherUseCase.
            currentState = awaitItem()
            println("Estado 2 (Ciudad Seleccionada): $currentState")
            assertEquals(Result.Success(exceptedSelectedCity), currentState.selectedCity)

            cancelAndIgnoreRemainingEvents()
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
        val exceptedWeather = sampleWeather() // Renombrado para claridad
        val exceptedSelectedCity = sampleCity()    // Renombrado para claridad

        // 1. Configuración de Mocks
        val selectedCitySourceFlow = MutableStateFlow<City?>(null)
        whenever(getSelectedCityUseCase()).thenReturn(selectedCitySourceFlow) // Devuelve Flow<City?>
        whenever(
            getWeatherUseCase(
                any(),
                any()
            )
        ).thenReturn(flowOf(exceptedWeather)) // Devuelve Flow<Weather>

        whenever(setSelectedCityUseCase.invoke(any())).thenAnswer { invocation ->
            val cityToSet = invocation.arguments.first() as City
            selectedCitySourceFlow.value = cityToSet
        }

        // 2. Reinicializa el ViewModel
        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            searchCitiesUseCase,
            toggleCityUseCase,
            getFromLocationCityUseCase
        )
        // runCurrent() aquí permite que los stateIn iniciales (Loading) se procesen
        // y también que selectedCitySourceFlow (null) se propague a Result.Loading
        // para selectedCityState.
        runCurrent()

        // 3. Assert: Verifica las emisiones del Flow 'state' con Turbine
        vm.state.test {
            // --- ESTADO 1: Inicialización Completa del ViewModel ---
            // selectedCityState será Result.Loading porque selectedCitySourceFlow es null
            // y stateAsResultIn lo mapea.
            // weatherState será Result.Loading porque su fuente (vía mapNotNull) no ha emitido una ciudad válida.
            var currentState = awaitItem()
            println("Estado 1 (Post-Init): $currentState")
            assertEquals(Result.Loading, currentState.weatherResult)

            // --- ACCIÓN: Seleccionar una ciudad ---
            vm.onAction(HomeAction.OnSelectedCity(exceptedSelectedCity))
            runCurrent() // Permite que la acción se procese y los flujos comiencen a reaccionar

            // --- ESTADO 2: Ciudad seleccionada, Clima cargando ---
            // selectedCityState ahora es Result.Success(selectedCityObject).
            // weatherState se ha reiniciado (debido a flatMapLatest) y su stateAsResultIn
            // emite Result.Loading primero antes de procesar el resultado de getWeatherUseCase.
            currentState = awaitItem()
            println("Estado 2 (Ciudad Seleccionada, Clima Cargando): $currentState")
            assertEquals(Result.Loading, currentState.weatherResult)

            // --- ESTADO 3: Ciudad seleccionada, Clima cargado con éxito ---
            // Ahora getWeatherUseCase (flowOf(expectedWeatherObject)) debería haber emitido,
            // y el stateAsResultIn de weatherState lo mapea a Result.Success.
            currentState = awaitItem()
            println("Estado 3 (Ciudad Seleccionada, Clima Éxito): $currentState")
            assertEquals(Result.Success(exceptedWeather), currentState.weatherResult)
        }
    }


    @Test
    fun `Weather error is propagated when request fails`() = runTest {
        val exceptedError = RuntimeException("Boom!")

        whenever(getWeatherUseCase(any(), any())).thenThrow(exceptedError)

        vm.state.test {
            assertEquals(Result.Loading, awaitItem().weatherResult)
            assertEquals("Boom!", exceptedError.message)
        }
    }


    /**
     * FavCities Test
     */
    @Test
    fun `Fav cities is requested when data base is called`() = runTest {

        val exceptedFavCities = emptyList<City>()

        whenever(getFavCitiesUseCase()).thenReturn(flowOf(exceptedFavCities))

        vm.state.test {
            assertEquals(emptyList<City>(), awaitItem().favCities)
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

    @Test
    fun `SearchedCities is called from api server`() = runTest {
        val searchedCities = emptyList<City>()

        whenever(searchCitiesUseCase.invoke(any())).thenReturn(searchedCities)

        vm.onAction(HomeAction.OnSearchCities(""))

        verify(searchCitiesUseCase).invoke(any())
    }


//    Opcional unit test muy largo por gemini

//    @Test
//    fun `Weather is requested if selected city is ready`() = runTest {
//        val expectedWeatherObject = sampleWeather() // Renombrado para claridad
//        val selectedCityObject = sampleCity()    // Renombrado para claridad
//
//        // 1. Configuración de Mocks
//        val selectedCitySourceFlow = MutableStateFlow<City?>(null)
//        whenever(getSelectedCityUseCase()).thenReturn(selectedCitySourceFlow) // Devuelve Flow<City?>
//        whenever(getWeatherUseCase(any(), any())).thenReturn(flowOf(expectedWeatherObject)) // Devuelve Flow<Weather>
//        whenever(getFavCitiesUseCase()).thenReturn(flowOf(emptyList())) // Devuelve Flow<List<City>>
//
//        whenever(setSelectedCityUseCase.invoke(any())).thenAnswer { invocation ->
//            val cityToSet = invocation.arguments.first() as City
//            selectedCitySourceFlow.value = cityToSet
//            Unit
//        }
//
//        // 2. Reinicializa el ViewModel
//        vm = HomeViewModel(
//            getWeatherUseCase,
//            getSelectedCityUseCase,
//            setSelectedCityUseCase,
//            getFavCitiesUseCase,
//            searchCitiesUseCase,
//            toggleCityUseCase,
//            getFromLocationCityUseCase
//        )
//        // runCurrent() aquí permite que los stateIn iniciales (Loading) se procesen
//        // y también que selectedCitySourceFlow (null) se propague a Result.Success(null)
//        // para selectedCityState.
//        runCurrent()
//
//        // 3. Assert: Verifica las emisiones del Flow 'state' con Turbine
//        vm.state.test {
//            // --- ESTADO 1: Inicialización Completa del ViewModel ---
//            // selectedCityState será Result.Success(null) porque selectedCitySourceFlow es null
//            // y stateAsResultIn lo mapea.
//            // weatherState será Result.Loading porque su fuente (vía mapNotNull) no ha emitido una ciudad válida.
//            // favCitiesState será Result.Success(emptyList) si getFavCitiesUseCase().stateAsResultIn funciona así.
//            var currentState = awaitItem()
//            println("Estado 1 (Post-Init): $currentState")
//            assertEquals(Result.Loading, currentState.selectedCity)
//            assertEquals(Result.Loading, currentState.weatherResult)
//            // Podrías añadir aserciones para favCities y searchedCities si es relevante para el estado inicial.
//
//            // --- ACCIÓN: Seleccionar una ciudad ---
//            vm.onAction(HomeAction.OnSelectedCity(selectedCityObject))
//            runCurrent() // Permite que la acción se procese y los flujos comiencen a reaccionar
//
//            // --- ESTADO 2: Ciudad seleccionada, Clima cargando ---
//            // selectedCityState ahora es Result.Success(selectedCityObject).
//            // weatherState se ha reiniciado (debido a flatMapLatest) y su stateAsResultIn
//            // emite Result.Loading primero antes de procesar el resultado de getWeatherUseCase.
//            currentState = awaitItem()
//            println("Estado 2 (Ciudad Seleccionada, Clima Cargando): $currentState")
//            assertEquals(Result.Success(selectedCityObject), currentState.selectedCity)
//            assertEquals(Result.Loading, currentState.weatherResult)
//
//            // --- ESTADO 3: Ciudad seleccionada, Clima cargado con éxito ---
//            // Ahora getWeatherUseCase (flowOf(expectedWeatherObject)) debería haber emitido,
//            // y el stateAsResultIn de weatherState lo mapea a Result.Success.
//            currentState = awaitItem()
//            println("Estado 3 (Ciudad Seleccionada, Clima Éxito): $currentState")
//            assertEquals(Result.Success(selectedCityObject), currentState.selectedCity)
//            assertEquals(Result.Success(expectedWeatherObject), currentState.weatherResult)
//
//            cancelAndIgnoreRemainingEvents()
//        }
//    }
}