package com.rndeveloper.myapplication.feature.forecast

import app.cash.turbine.test
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class ForecastViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getWeatherUseCase: GetWeatherUseCase

    private lateinit var vm: ForecastViewModel

    val weather = sampleWeather()

    @Before
    fun setUp() {
        whenever(getWeatherUseCase(any(), any())).thenReturn(flowOf(weather))
        vm = ForecastViewModel(
            "CityName",
            "-34.6037",
            "-58.3816",
            getWeatherUseCase
        )

    }

    @Test
    fun `UI is updated with the weather on start`() = runTest {

        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(weather), awaitItem())
        }
    }

}