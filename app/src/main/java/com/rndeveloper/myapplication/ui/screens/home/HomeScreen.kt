package com.rndeveloper.myapplication.ui.screens.home

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.draw.alpha
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
import com.rndeveloper.myapplication.Result
import com.rndeveloper.myapplication.common.PermissionRequestEffect
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.ifSuccess
import com.rndeveloper.myapplication.ui.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.components.TopAppBar
import com.rndeveloper.myapplication.ui.screens.home.components.FavCitiesContent
import com.rndeveloper.myapplication.ui.screens.home.components.FavouriteIconButtonContent
import com.rndeveloper.myapplication.ui.screens.home.components.SearchContent
import com.rndeveloper.myapplication.ui.theme.DevExpertWeatherTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeViewModel,
    onForecastClick: (String, String, String) -> Unit = { _, _, _ -> }
) {

    val state by vm.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    var message by rememberSaveable { mutableStateOf("") }
    val onAction = vm::onAction

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
        } else {
            message = "Location permission denied"
        }
    }

    Scaffold(
        topBar = {
            val isSelectedCityFav = state.favCities.contains(state.selectedCity)
            TopAppBar(
                title = { // Ubicación y fecha
                    when (state.currentWeather) {
                        is Result.Loading -> LoadingAnimation(modifier = Modifier.fillMaxSize())
                        is Result.Success -> {
                            TopAppBar(
                                title = "${state.selectedCity?.name}, ${state.selectedCity?.country}",
                                subtitle = (state.currentWeather as Result.Success<CurrentWeather?>).data?.date
                                    ?: ""
                            )
                        }
                        is Result.Error -> TODO()
                    }
                },
                actions = {
                    FavouriteIconButtonContent(isFav = isSelectedCityFav) {
                        onAction(
                            HomeAction.OnToggleCity(
                                state.selectedCity!!,
                                isSelectedCityFav
                            )
                        )
                    }
                }
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
fun BlinkingText() {
    val alpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0.1f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(25.dp)
            .alpha(alpha)
            .background(Color.Gray, RoundedCornerShape(4.dp))
    )
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
        FavCitiesContent(state = state, onAction = onAction)
        Spacer(modifier = Modifier.height(15.dp))
        SearchContent(
            keyboardController = keyboardController,
            state = state,
            onAction = onAction
        )
        when (state.currentWeather) {
            is Result.Loading -> LoadingAnimation(modifier = Modifier.fillMaxSize())
            is Result.Error -> Text(text = "Error: ${state.currentWeather.exception.message}")
            is Result.Success -> {
                Spacer(modifier = Modifier.height(40.dp))
                WeatherMainContent(currentWeather = state.currentWeather.data)
                Spacer(modifier = Modifier.height(20.dp))
                WeatherDetailsContent(currentWeather = state.currentWeather.data)
            }
        }
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
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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