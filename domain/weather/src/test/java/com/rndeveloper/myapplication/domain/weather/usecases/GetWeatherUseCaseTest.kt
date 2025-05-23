package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.sampleWeather
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetWeatherUseCaseTest {

    @Test
    fun `Invoke calls repository`() {
        val weatherFlow = flowOf(
            sampleWeather(0.0, 0.0)
        )

        val useCase = GetWeatherUseCase(mock {
            on { weather(lat = 0.0, lon = 0.0) } doReturn weatherFlow
        })

        val result = useCase(0.0, 0.0)

        assertEquals(weatherFlow, result)
    }
}
