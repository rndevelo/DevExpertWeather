package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.sampleCity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class GetLocationCityUseCaseTest {

//    Fixme: Configurar el caso de uso

    @Test
    fun `Invoke calls repository`(): Unit = runBlocking {
        val locationCity = sampleCity()

        val useCase = GetFromLocationCityUseCase(
            mock {
                onBlocking {
                    cityByLastLocation()
                } doReturn locationCity
            }
        )

        val result = useCase()

        assertEquals(locationCity, result)
    }
}