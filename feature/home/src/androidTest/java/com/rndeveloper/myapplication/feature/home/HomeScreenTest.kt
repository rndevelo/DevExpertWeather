package com.rndeveloper.myapplication.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.rndeveloper.myapplication.domain.sampleCity
import com.rndeveloper.myapplication.domain.sampleWeather
import com.rndeveloper.myapplication.feature.common.LOADING_ANIMATION_TAG
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.feature.home.composables.SEARCH_TEXT_FIELD_TAG
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenSelectedCityLoadingState_showsLoadingAnimation(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(state = HomeViewModel.UiState(selectedCity = Result.Loading))
        }

        onNodeWithTag(LOADING_ANIMATION_TAG).assertIsDisplayed()
    }

    @Test
    fun whenSelectedCityErrorState_showsErrorText(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Error(
                        RuntimeException(
                            "An error occurred"
                        )
                    )
                )
            )
        }

        onNodeWithText("An error occurred").assertExists()
    }

    @Test
    fun whenSelectedCitySuccessState_showSelectedCity(): Unit = with(composeTestRule) {
        val selectedCity = sampleCity()
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(selectedCity),
                    weather = Result.Success(sampleWeather())
                )
            )
        }

        onNodeWithText("${selectedCity.name}, ${selectedCity.country}").assertExists()
    }

    @Test
    fun whenWeatherLoadingState_showsLoadingAnimation(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Loading
                )
            )
        }

        onNodeWithTag(LOADING_ANIMATION_TAG).assertIsDisplayed()
    }

    @Test
    fun whenWeatherErrorState_showsErrorText(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Error(
                        RuntimeException(
                            "An error occurred"
                        )
                    )
                )
            )
        }

        onNodeWithText("An error occurred").assertExists()
    }

    @Test
    fun whenWeatherSuccessState_showWeather(): Unit = with(composeTestRule) {
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather())
                )
            )
        }

        onNodeWithText("Sunny").assertExists()
    }

    @Test
    fun whenFavCitiesState_showWeather(): Unit = with(composeTestRule) {
        val sampleCities = listOf(sampleCity(), sampleCity(40.7128, -74.0060, "New York", "USA"))
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather()),
                    favCities = sampleCities
                )
            )
        }

        onNodeWithText("New York").assertExists()
    }

    @Test
    fun whenFavCityClicked_listenerIsCalled(): Unit = with(composeTestRule) {
        var clickedCity = sampleCity()
        val sampleCities = listOf(sampleCity(), sampleCity(40.7128, -74.0060, "New York", "USA"))
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather()),
                    favCities = sampleCities,
                ),
                onAction = {
                    if (it is HomeAction.OnSelectedCity) {
                        clickedCity = it.city
                    }
                }
            )
        }

        onNodeWithText("New York").performClick()

        assertEquals(sampleCities[1], clickedCity)
    }

    @Test
    fun whenSearchedCitiesState_showWeather(): Unit = with(composeTestRule) {

        val sampleCities = listOf(
            sampleCity(),
            sampleCity(40.7128, -74.0060, "New York", "USA"),
            sampleCity(35.6895, 139.6917, "Tokyo", "Japan")
        )

        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather()),
                    searchedCities = sampleCities,
                ),
                onAction = {
                    if (it is HomeAction.OnSearchCities) {
//                        clickedCity = it.query
                    }
                }
            )
        }

        onNodeWithTag(SEARCH_TEXT_FIELD_TAG).performTextInput("New")

        // Verifica que aparece "New York"
        onNodeWithText("New York, USA").assertIsDisplayed()

        // Verifica que no aparece una ciudad que no contiene "New"
        onNodeWithText("Tokyo").assertDoesNotExist()
    }

    @Test
    fun whenFavoriteClicked_listenerIsCalled(): Unit = with(composeTestRule) {
        var clicked = false
        setContent {

            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather()),
                ),
                onAction = {
                    if (it is HomeAction.OnToggleCity) {
                        clicked = true
                    }
                }
            )

        }

        onNodeWithContentDescription(Icons.Default.Favorite.toString()).performClick()
        assertTrue(clicked)
    }

    @Test
    fun whenNavigateClicked_listenerIsCalled(): Unit = with(composeTestRule) {
        var clicked = false
        setContent {
            HomeScreen(
                state = HomeViewModel.UiState(
                    selectedCity = Result.Success(sampleCity()),
                    weather = Result.Success(sampleWeather()),
                ),
                onForecastClick = { _, _, _ -> clicked = true }
            )
        }

        onNodeWithTag(FLOATING_ACTION_BUTTON_TAG).performClick()
        assertTrue(clicked)
    }
}
