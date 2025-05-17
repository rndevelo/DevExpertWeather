package com.rndeveloper.myapplication.feature.home

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.feature.common.ErrorText
import com.rndeveloper.myapplication.feature.common.LoadingAnimation
import com.rndeveloper.myapplication.feature.common.MyTopAppBar
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.feature.common.Screen
import com.rndeveloper.myapplication.feature.common.theme.DevExpertWeatherTheme
import com.rndeveloper.myapplication.feature.home.composables.FavCitiesContent
import com.rndeveloper.myapplication.feature.home.composables.FavouriteIconButtonContent
import com.rndeveloper.myapplication.feature.home.composables.PermissionRequestEffect
import com.rndeveloper.myapplication.feature.home.composables.SearchContent
import com.rndeveloper.myapplication.feature.home.composables.ShowDialogIfPermissionIsDenied


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    onForecastClick: (String, String, String) -> Unit = { _, _, _ -> }
) {

    val state by vm.uiState.collectAsState()
    var isLocationPermissionDenied by remember { mutableStateOf(false) }

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) {
        if (state.selectedCity == null) {
            if (it) {
                vm.onAction(HomeAction.OnGetCityByLocation)
            } else {
                isLocationPermissionDenied = true
            }
        }
    }

    Screen {

        ShowDialogIfPermissionIsDenied(
            selectedCity = state.selectedCity,
            favCities = state.favCities,
            searchedCities = state.searchedCities,
            isLocationPermissionDenied = isLocationPermissionDenied,
            onIsLocationPermissionDenied = { isLocationPermissionDenied = false },
            onAction = vm::onAction
        )

        if (state.isLoading && !isLocationPermissionDenied) {
            LoadingAnimation(modifier = Modifier.fillMaxSize())
        }
        when (state.weatherResult) {
            is Result.Loading -> {}
            is Result.Success -> {
                val weather = (state.weatherResult as Result.Success<Weather>).data.current
                if (state.selectedCity != null) {
                    HomeContent(
                        selectedCity = state.selectedCity!!,
                        date = weather.date,
                        weatherDescription = weather.weatherDescription,
                        weatherIcon = weather.weatherIcon,
                        temperature = weather.temperature,
                        humidity = weather.humidity,
                        windSpeed = weather.windSpeed,
                        precipitation = weather.precipitation,
                        favCities = state.favCities,
                        searchedCities = state.searchedCities,
                        onAction = vm::onAction,
                        onForecastClick = {
                            onForecastClick(
                                state.selectedCity?.name ?: "",
                                state.selectedCity?.lat.toString(),
                                state.selectedCity?.lon.toString()
                            )
                        }
                    )
                }
            }

            is Result.Error -> {
                ErrorText(error = (state.weatherResult as Result.Error).exception)
                Log.d(
                    "WeatherError",
                    "HomeScreen: ${(state.weatherResult as Result.Error).exception}"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    selectedCity: City,
//    Weather parameters
    date: String,
    weatherDescription: String,
    weatherIcon: String,
    temperature: Double,
    humidity: Int,
    windSpeed: Double,
    precipitation: Double,
    favCities: List<City>,
    searchedCities: List<City>,
    onAction: (HomeAction) -> Unit,
    onForecastClick: () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { // Ubicación y fecha
                    MyTopAppBar(
                        title = "${selectedCity.name}, ${selectedCity.country}",
                        subtitle = date
                    )
                },
                actions = {
                    val isSelectedCityFav = favCities.contains(selectedCity)
                    FavouriteIconButtonContent(isFav = isSelectedCityFav) {
                        onAction(
                            HomeAction.OnToggleCity(
                                selectedCity,
                                isSelectedCityFav
                            )
                        )
                    }
                }
            )

        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.forecast_text_7_day_forecast)) },
                icon = { Text("\uD83D\uDCCA") },
                onClick = onForecastClick,
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { keyboardController?.hide() }
                    ) // Oculta el teclado al tocar fuera
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FavCitiesContent(
                favCities = favCities,
                selectedCity = selectedCity,
                onAction = onAction
            )
            SearchContent(
                keyboardController = keyboardController,
                favCities = favCities,
                searchedCities = searchedCities,
                onAction = onAction
            )
            Spacer(modifier = Modifier.height(40.dp))
            WeatherMainContent(
                weatherDescription = weatherDescription,
                weatherIcon = weatherIcon,
                temperature = temperature
            )
            Spacer(modifier = Modifier.height(25.dp))
            WeatherDetailsContent(
                humidity = humidity,
                windSpeed = windSpeed,
                precipitation = precipitation
            )
        }
    }
}

// Información principal del clima
@Composable
private fun WeatherMainContent(
    weatherDescription: String,
    weatherIcon: String,
    temperature: Double,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = weatherIcon,
            fontSize = 90.sp,
        )
        Text(
            text = "${temperature}°C",
            fontSize = 45.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = weatherDescription,
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

// Detalles del clima
@Composable
private fun WeatherDetailsContent(
    humidity: Int,
    windSpeed: Double,
    precipitation: Double,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherDetailRow(
                label = stringResource(R.string.home_text_humidity),
                "$humidity%"
            )
            WeatherDetailRow(
                label = stringResource(R.string.home_text_wind_speed),
                "$windSpeed km/h"
            )
            WeatherDetailRow(
                label = stringResource(R.string.home_text_precipitation),
                "$precipitation mm"
            )
        }
    }
}

@Composable
fun WeatherDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DevExpertWeatherTheme {
//        HomeScreen(vm = viewModel())
    }
}