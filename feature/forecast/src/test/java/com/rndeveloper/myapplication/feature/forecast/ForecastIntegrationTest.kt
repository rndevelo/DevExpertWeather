package com.rndeveloper.myapplication.feature.forecast

import app.cash.turbine.test
import com.rndeveloper.myapplication.data.buildWeatherRepositoryWith
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import com.rndeveloper.myapplication.feature.common.Result
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForecastIntegrationTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var vm: ForecastViewModel

    @Before
    fun setup() {
        val weatherRepository = buildWeatherRepositoryWith(
            weatherLocalData = sampleWeather(),
            weatherRemoteData = sampleWeather()
        )

        vm = ForecastViewModel(
            "CityName",
            "-34.6037",
            "-58.3816",
            GetWeatherUseCase(weatherRepository)
        )
    }

    @Test
    fun `UI is updated with the weather on start`() = runTest {
        vm.weatherState.test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(sampleWeather()), awaitItem())
        }
    }
}