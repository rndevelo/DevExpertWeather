package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.domain.sampleWeather
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
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

//    @Test
//    fun `Weather are saved to local data source when it's empty`(): Unit = runBlocking {
//        val localWeather = null
//        val remoteWeather = sampleWeather(-34.6037, -58.3816, System.currentTimeMillis())
//
//        whenever(weatherLocalDataSource.weather).thenReturn(flowOf(localWeather))
//        whenever(weatherRemoteDataSource.getWeather(lat = -34.6037, lon = -58.3816)).thenReturn(remoteWeather)
//
//        repository.weather(lat = -34.6037, lon = -58.3816).first()
//
//        verify(weatherLocalDataSource).insertWeather(remoteWeather)
//    }

    @Test
    fun `Weather is saved to local data source when it's empty`(): Unit = runBlocking {

        // Configuración para el Flow weatherLocalDataSource.weather
        // Esto simula el comportamiento de un Flow de Room:
        // 1. Inicialmente emite null (o lo que sea que tengas en la BD, aquí simulamos vacío)
        // 2. Después de una inserción, debería emitir el nuevo valor.
        // Usaremos un MutableStateFlow en el mock para simular esto de forma reactiva.

        val localWeatherFlow = MutableStateFlow<Weather?>(null) // Comienza vacío
        val remoteWeather = sampleWeather(lat = 0.0, lon = 0.0)


        whenever(weatherLocalDataSource.weather(lat = -34.6037, lon = -58.3816)).thenReturn(localWeatherFlow) // El mock devuelve este Flow mutable
        whenever(weatherRemoteDataSource.getWeather(lat = -34.6037, lon = -58.3816)).thenReturn(remoteWeather)

        // Simulamos lo que hace `insertWeather`: actualiza el Flow local
        whenever(weatherLocalDataSource.insertWeather(remoteWeather)).thenAnswer {
            localWeatherFlow.value = remoteWeather // Cuando insertWeather es llamado, actualiza el StateFlow
            Unit // insertWeather es suspend fun que devuelve Unit
        }

        // Act
        val resultData = repository.weather(lat = -34.6037, lon = -58.3816).first() // .first() recolectará el Flow

        // Assert
        assertEquals(remoteWeather, resultData)
        verify(weatherLocalDataSource).insertWeather(remoteWeather)
        verify(weatherRemoteDataSource).getWeather(lat = -34.6037, lon = -58.3816)
    }
}
