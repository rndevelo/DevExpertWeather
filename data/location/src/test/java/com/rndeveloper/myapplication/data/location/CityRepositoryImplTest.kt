package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.sampleCity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@RunWith(MockitoJUnitRunner::class)
class CityRepositoryImplTest {

    @Mock
    private lateinit var cityLocalDataSource: CityLocalDataSource

    @Mock
    private lateinit var cityRemoteDataSource: CityRemoteDataSource

    private lateinit var repository: CityRepositoryImpl

    @Before
    fun setUp() {
        repository =
            CityRepositoryImpl(
                cityLocalDataSource = cityLocalDataSource,
                cityRemoteDataSource = cityRemoteDataSource
            )
    }

    @Test
    fun `Selected city are taken from local data source to save cache`() = runBlocking {
        val selectedCity = null

        whenever(cityLocalDataSource.selectedCity).thenReturn(flowOf(selectedCity))

        val result = repository.selectedCity

        assertEquals(selectedCity, result.first())
    }

    @Test
    fun `Selected city is saved to local data source to save cache`() = runBlocking {
        val selectedCity = sampleCity()

        repository.insertSelectedCity(selectedCity)

        verify(cityLocalDataSource).insertSelectedCity(selectedCity)
    }

    @Test
    fun `Fav cities are taken from local data source`() = runBlocking {
        val favCities = emptyList<City>()

        whenever(cityLocalDataSource.favCities).thenReturn(flowOf(favCities))

        val result = repository.favCities

        assertEquals(favCities, result.first())
    }

    @Test
    fun `Search cities by name from retrofit api`() = runBlocking {

        val searchedCities = emptyList<City>()

        whenever(cityRemoteDataSource.searchCities("Buenos Aires")).thenReturn(searchedCities)

        val result = repository.searchCities("Buenos Aires")

        assertEquals(searchedCities, result)
    }

    @Test
    fun `Toggling favourite city insert local data base`(): Unit = runBlocking {
        val city = sampleCity()

        repository.toggleFavCity(city, false)

        verify(cityLocalDataSource).insertFavCity(argThat { lat == city.lat && lon == city.lon })
    }

    @Test
    fun `Toggling favourite city delete local data base`(): Unit = runBlocking {
        val city = sampleCity(40.7128, -74.0060) // Nueva ciudad New York

        repository.toggleFavCity(city, true)

        verify(cityLocalDataSource).deleteFavCity(argThat { lat == city.lat && lon == city.lon })
    }
}