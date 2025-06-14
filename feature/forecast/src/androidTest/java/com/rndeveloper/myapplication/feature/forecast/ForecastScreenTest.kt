package com.rndeveloper.myapplication.feature.forecast

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.feature.common.LOADING_ANIMATION_TAG
import com.rndeveloper.myapplication.feature.common.Result
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ForecastScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenSelectedCityLoadingState_showsLoadingAnimation(): Unit = with(composeTestRule) {
        setContent {
            ForecastScreen(state = ForecastViewModel.UiState(weather = Result.Loading))
        }

        onNodeWithTag(LOADING_ANIMATION_TAG).assertIsDisplayed()
    }

    @Test
    fun whenSelectedCityErrorState_showsErrorText(): Unit = with(composeTestRule) {
        setContent {
            ForecastScreen(
                state = ForecastViewModel.UiState(
                    weather = Result.Error(
                        RuntimeException("An error occurred")
                    )
                )
            )
        }

        onNodeWithText("An error occurred").assertExists()
    }

    @Test
    fun whenSelectedCitySuccessState_showWeather(): Unit = with(composeTestRule) {
        setContent {
            ForecastScreen(
                state = ForecastViewModel.UiState(
                    weather = Result.Success(sampleWeather())
                )
            )
        }

        onNodeWithText("Cloudy").assertExists()
    }

    @Test
    fun whenNavigateClicked_listenerIsCalled(): Unit = with(composeTestRule) {
        var clicked = false
        setContent {
            ForecastScreen(
                state = ForecastViewModel.UiState(
                    weather = Result.Success(sampleWeather())
                ),
                onBack = { clicked = true }
            )
        }

        onNodeWithContentDescription(Icons.AutoMirrored.Filled.ArrowBack.toString()).performClick()
        assertTrue(clicked)
    }
}