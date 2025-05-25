package com.rndeveloper.myapplication.feature.home

import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetFromLocationCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.ToggleCityUseCase
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var vm: HomeViewModel

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

    @Before
    fun setUp() {
        whenever(getFavCitiesUseCase()).then {
            flowOf(
                listOf(
                    sampleCity(),
                    sampleCity()
                )
            )
        }
        whenever(getSelectedCityUseCase()).then {
            flowOf(sampleCity())
        }
        vm = HomeViewModel(
            getWeatherUseCase,
            getSelectedCityUseCase,
            setSelectedCityUseCase,
            getFavCitiesUseCase,
            searchCitiesUseCase,
            toggleCityUseCase,
            getFromLocationCityUseCase,
            coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `OnAction - OnSearchCities - return expected value`() = runTest {

        val expected = emptyList<City>()

        whenever(searchCitiesUseCase.invoke(any())).then { expected }

        vm.onAction(HomeAction.OnSearchCities(""))

//        runCurrent()

        assertEquals(expected, vm.searchedCitiesState.value)
    }

    @Test
    fun `OnAction - OnSelectedCity - calls setSelectedCityUseCase with correct city`() = runTest {
        // runTest usará automáticamente el TestDispatcher de coroutinesTestRule
        // si está configurado como el dispatcher principal en la regla.

        // 1. Arrange: Define los datos de entrada
        val expectedCity = City(name = "Test City", country = "TC", lat = 1.0, lon = 2.0)
        val action = HomeAction.OnSelectedCity(expectedCity)

        // 2. Act: Ejecuta la acción en el ViewModel
        vm.onAction(action)

        // No necesitas runCurrent() aquí si setSelectedCityUseCase no es suspend
        // o si no necesitas avanzar explícitamente el dispatcher para esta verificación particular.
        // runTest se encarga de que la coroutine lanzada por onAction se complete.

        runCurrent()

        // 3. Assert: Verifica que el mock fue invocado correctamente
        // Verifica que el METODO 'invoke' del mock 'setSelectedCityUseCase' fue llamado con 'expectedCity'.
        // Si setSelectedCityUseCase es una función suspendida, Mockito-Kotlin lo maneja bien.
        verify(setSelectedCityUseCase).invoke(expectedCity)
    }
}