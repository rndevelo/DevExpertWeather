package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.data.location.datasources.RegionDataSource
import com.rndeveloper.myapplication.domain.sampleCity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import junit.framework.TestCase.assertEquals


@RunWith(MockitoJUnitRunner::class)
class RegionRepositoryImplTest {

    @Mock
    lateinit var regionDataSource: RegionDataSource

    private lateinit var repository: RegionRepositoryImpl

    @Before
    fun setUp() {
        repository = RegionRepositoryImpl(regionDataSource)
    }

    @Test
    fun `Region is found from location provider data`(): Unit = runBlocking {
        val cityFromLocation = sampleCity()

        whenever(regionDataSource.findLastLocationCityInfo()).thenReturn(cityFromLocation)

        val result = repository.cityByLastLocation()

        assertEquals(cityFromLocation, result)
    }
}