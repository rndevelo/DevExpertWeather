package com.rndeveloper.myapplication.feature.home

import com.rndeveloper.myapplication.domain.location.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetFromLocationCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.ToggleCityUseCase
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @Test
    fun `Weather are not requested if selected city is not ready`() = runTest {
        vm.state.first()

        runCurrent()

        verify(getWeatherUseCase, times(0)).invoke(sampleCity().lat, sampleCity().lon)
    }

    

}