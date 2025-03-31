package com.rndeveloper.myapplication.ui.screens.home

import android.Manifest
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rndeveloper.myapplication.common.PermissionRequestEffect
import com.rndeveloper.myapplication.common.getLocation
import com.rndeveloper.myapplication.common.getLocalityAndCountry
import com.rndeveloper.myapplication.data.CityInfo
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.ui.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.components.TopAppBar
import com.rndeveloper.myapplication.ui.screens.home.components.SearchContent
import com.rndeveloper.myapplication.ui.theme.DevExpertWeatherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: HomeViewModel = viewModel(), onForecastClick: (String, String, String) -> Unit) {

    val ctx = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val state by vm.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.selectedCityInfo) {
        if (state.selectedCityInfo != null) {
            vm.onUiReady(
                lat = state.selectedCityInfo?.latitude!!,
                lon = state.selectedCityInfo?.longitude!!
            )
        }
    }

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
        coroutineScope.launch {
            if (granted) {
                ctx.getLocation().let { location ->
                    if (location != null) {
                        vm.onSelectedCityInfo(
                            cityInfo = CityInfo().copy(
                                name = ctx.getLocalityAndCountry().first,
                                country = ctx.getLocalityAndCountry().second,
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                        return@launch
                    } else {
                        Toast.makeText(ctx, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(ctx, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { // Ubicación y fecha
                    TopAppBar(
                        title = "${state.selectedCityInfo?.name}, ${state.selectedCityInfo?.country}",
                        subtitle = state.currentWeather?.date ?: ""
                    )
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Pronóstico") },
                icon = { Text("\uD83D\uDCCA") },
                onClick = {
                    onForecastClick(
                        state.selectedCityInfo?.name ?: "",
                        state.selectedCityInfo?.latitude.toString(),
                        state.selectedCityInfo?.longitude.toString()
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
            onSearchCity = vm::onSearchCities,
            onSelectedCityInfo = vm::onSelectedCityInfo,
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
    onSearchCity: (String) -> Unit,
    onSelectedCityInfo: (CityInfo) -> Unit,
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
        SearchContent(
            keyboardController = keyboardController,
            citiesInfo = state.citiesInfo,
            onSearchCity = onSearchCity,
            onSelectedCityInfo = onSelectedCityInfo,
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
            WeatherDetailRow(label = "🌡️ Temperatura", "${currentWeather?.temperature}°C")
            WeatherDetailRow(label = "💧 Humedad", "${currentWeather?.humidity}%")
            WeatherDetailRow(label = "\uD83D\uDCA8 Viento", "${currentWeather?.windSpeed} km/h")
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DevExpertWeatherTheme {
//        HomeScreen { -> a, b, c }
    }
}