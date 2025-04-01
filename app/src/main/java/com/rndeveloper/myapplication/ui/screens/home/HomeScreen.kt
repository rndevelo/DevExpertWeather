package com.rndeveloper.myapplication.ui.screens.home

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rndeveloper.myapplication.R
import com.rndeveloper.myapplication.common.PermissionRequestEffect
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.ui.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.components.TopAppBar
import com.rndeveloper.myapplication.ui.screens.home.components.SearchContent
import com.rndeveloper.myapplication.ui.theme.DevExpertWeatherTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    onForecastClick: (String, String, String) -> Unit = { _, _, _ -> }
) {

    val state by vm.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var message by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state.selectedCity) {
        if (state.selectedCity != null) {
            vm.onAction(
                HomeAction.OnGetWeather(
                    lat = state.selectedCity?.latitude!!,
                    lon = state.selectedCity?.longitude!!
                )
            )
        }
    }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(message)
            message = ""
        }
    }

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) {
        if (it) {
            vm.onAction(HomeAction.OnGetCityByLocation)
            message = "Location permission granted"
        } else {
            message = "Location permission denied"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { // Ubicación y fecha
                    TopAppBar(
                        title = "${state.selectedCity?.name}, ${state.selectedCity?.country}",
                        subtitle = state.currentWeather?.date ?: ""
                    )
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.home_text_forecast)) },
                icon = { Text("\uD83D\uDCCA") },
                onClick = {
                    onForecastClick(
                        state.selectedCity?.name ?: "",
                        state.selectedCity?.latitude.toString(),
                        state.selectedCity?.longitude.toString()
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { paddingValues ->

        LoadingAnimation(
            isLoading = state.loading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

        HomeContent(
            state = state,
            onAction = vm::onAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
        )
    }
}

@Composable
fun HomeContent(
    state: HomeViewModel.UiState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { keyboardController?.hide() }
                ) // Oculta el teclado al tocar fuera
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(state.favCities) {
                Card(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.name, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        SearchContent(
            keyboardController = keyboardController,
            searchedCities = state.searchedCities,
            onAction = onAction
        )
        Spacer(modifier = Modifier.height(50.dp))
        WeatherMainContent(state.currentWeather)
        Spacer(modifier = Modifier.height(50.dp))
        WeatherDetailsContent(currentWeather = state.currentWeather)
    }
}

// Información principal del clima
@Composable
private fun WeatherMainContent(currentWeather: CurrentWeather?) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = currentWeather?.weatherIcon ?: "",
            fontSize = 90.sp,
        )
        Text(
            text = "${currentWeather?.temperature}°C",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = currentWeather?.weatherDescription ?: "",
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}

// Detalles del clima
@Composable
private fun WeatherDetailsContent(currentWeather: CurrentWeather?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            WeatherDetailRow(
                label = stringResource(R.string.home_text_temperature),
                "${currentWeather?.temperature}°C"
            )
            WeatherDetailRow(
                label = stringResource(R.string.home_text_humidity),
                "${currentWeather?.humidity}%"
            )
            WeatherDetailRow(
                label = stringResource(R.string.home_text_wind_speed),
                "${currentWeather?.windSpeed} km/h"
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
        HomeScreen(vm = viewModel())
    }
}