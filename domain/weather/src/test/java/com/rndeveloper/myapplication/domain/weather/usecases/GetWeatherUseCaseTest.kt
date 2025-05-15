package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.weather.model.Weather
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.junit.Test
import junit.framework.TestCase.assertEquals

class GetWeatherUseCaseTest {

    @Test
    fun `Invoke calls repository`() {
        val weatherFlow = flowOf(
            Weather(
                current = mock(),
                forecast = emptyList(),
                lastUpdated = 0,
                lat = 0.0,
                lon = 0.0
            )
        )

        val useCase = GetWeatherUseCase(mock {
            on { weather(lat = 0.0, lon = 0.0) } doReturn weatherFlow
        })

        val result = useCase(0.0, 0.0)

        assertEquals(weatherFlow, result)
    }
}