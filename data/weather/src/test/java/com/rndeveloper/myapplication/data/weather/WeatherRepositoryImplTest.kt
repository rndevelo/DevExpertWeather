package com.rndeveloper.myapplication.data.weather

import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryImplTest {

    @Mock
    lateinit var weatherLocalDataSource: WeatherLocalDataSource

    @Mock
    lateinit var weatherRemoteDataSource: WeatherRemoteDataSource

    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setUp() {
        repository = WeatherRepositoryImpl(
            weatherLocalDataSource = weatherLocalDataSource,
            weatherRemoteDataSource = weatherRemoteDataSource
        )
    }

//    City(name = "Buenos Aires", country = "Argentina", lat = -34.6037, lon = -58.3816)


    @Test
    fun `Current and forecast weather are taken from local data source if available`(): Unit =
        runBlocking {
            val localWeather = sampleWeather(-34.6037, -58.3816)

            whenever(weatherLocalDataSource.weather(lat = -34.6037, lon = -58.3816)).thenReturn(flowOf(localWeather))

            val result = repository.weather(lat = -34.6037, lon = -58.3816)

            assertEquals(localWeather, result.first())
        }

    @Test
    fun `Weather are saved to local data source when it's empty`(): Unit = runBlocking {
        val localWeather = null
        val remoteWeather = sampleWeather(-34.6037, -58.3816)

        whenever(weatherLocalDataSource.weather(lat = -34.6037, lon = -58.3816)).thenReturn(flowOf(localWeather))
        whenever(weatherRemoteDataSource.getWeather(lat = -34.6037, lon = -58.3816)).thenReturn(remoteWeather)

        repository.weather(lat = -34.6037, lon = -58.3816).first()

        verify(weatherLocalDataSource).insertWeather(remoteWeather)
    }
}
