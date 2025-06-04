package com.rndeveloper.myapplication

import androidx.test.rule.GrantPermissionRule
import com.rndeveloper.myapplication.domain.location.CityRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_FINE_LOCATION"
    )

    @Inject
    lateinit var cityRepository: CityRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_it_works() {
        runBlocking {
            val favCities = cityRepository.favCities.first()
            assertTrue(favCities.isEmpty())
        }
    }
}