package com.rndeveloper.myapplication.domain.location.usecases

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


class GetFavCitiesUseCaseTest {

    @Test
    fun `Invoke calls repository`() {
        val favCitiesFlow = flowOf(
            listOf(
                sampleCity(),
                sampleCity()
            )
        )

        val useCase = GetFavCitiesUseCase(mock {
            on { favCities } doReturn favCitiesFlow
        })
        
        val result = useCase()

        assertEquals(favCitiesFlow, result)
    }
}