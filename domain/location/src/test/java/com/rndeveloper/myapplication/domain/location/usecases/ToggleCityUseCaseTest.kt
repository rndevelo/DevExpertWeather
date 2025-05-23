package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.CityRepository
import com.rndeveloper.myapplication.domain.sampleCity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify


class ToggleCityUseCaseTest {

    @Test
    fun `Invoke calls repository`() = runBlocking {
        val city = sampleCity()
        val isFav = true

        val repository = mock<CityRepository>()
        val useCase = ToggleCityUseCase(repository)

        useCase(city, isFav)

        verify(repository).toggleFavCity(city, isFav)
    }
}
