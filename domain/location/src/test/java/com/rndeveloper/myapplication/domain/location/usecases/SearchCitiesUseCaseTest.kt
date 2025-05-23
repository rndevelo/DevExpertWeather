package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.sampleCity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class SearchCitiesUseCaseTest {

    @Test
    fun `Invoke calls repository`(): Unit = runBlocking {
        val cities = listOf(
            sampleCity(),
            sampleCity()
        )

        val useCase = SearchCitiesUseCase(mock {
            on { runBlocking { searchCities("") } } doReturn cities
        })

        val result = useCase("")

        assertEquals(cities, result)
    }
}