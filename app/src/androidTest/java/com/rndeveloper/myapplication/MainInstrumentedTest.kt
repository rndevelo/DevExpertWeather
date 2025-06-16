package com.rndeveloper.myapplication

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.rule.GrantPermissionRule
import com.rndeveloper.myapplication.data.server.MockWebServerRule
import com.rndeveloper.myapplication.data.server.fromJson
import com.rndeveloper.myapplication.feature.home.FLOATING_ACTION_BUTTON_TAG
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class MainInstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val mockWebServerRule = MockWebServerRule()


    @get:Rule(order = 2)
    val locationPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.ACCESS_COARSE_LOCATION"
    )

    @get:Rule(order = 3)
    val androidComposeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        mockWebServerRule.server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                println("➡️ Request received: ${request.method} ${request.path}")

                return when {
                    request.path?.contains("/search") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .fromJson("searched_cities.json")
                    }

                    request.path?.contains("/forecast") == true -> {
                        MockResponse()
                            .setResponseCode(200)
                            .fromJson("weather_forecast.json")
                    }

                    else -> {
                        println("❌ Unexpected request: ${request.path}")
                        MockResponse().setResponseCode(404)
                    }
                }
            }
        }

        hiltRule.inject()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun click_a_floating_button_navigates_to_forecast(): Unit = with(androidComposeRule) {

        onRoot(useUnmergedTree = true).printToLog("UI_TREE")

        waitUntil(5_000) {
            onAllNodesWithText("C", substring = true, useUnmergedTree = true).fetchSemanticsNodes().isNotEmpty()
        }
        onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).performClick()

        onNodeWithText("7-day forecast", useUnmergedTree = true).assertIsDisplayed()
    }
}
