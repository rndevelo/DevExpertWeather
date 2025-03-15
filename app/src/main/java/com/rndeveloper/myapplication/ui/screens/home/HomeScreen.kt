package com.rndeveloper.myapplication.ui.screens.home

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.rndeveloper.myapplication.common.PermissionRequestEffect
import com.rndeveloper.myapplication.common.getRegion
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.ui.theme.DevExpertWeatherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: HomeViewModel = viewModel()) {

    val state = vm.state
    val ctx = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val uiState = state.currentWeather

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
        coroutineScope.launch {
            val locality = if (granted) ctx.getRegion() else "Madrid"
            vm.onUiReady(locality)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { // Ubicación y fecha
                    Column {
                        Text(
                            "${uiState.cityName}, ${uiState.country}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(uiState.localTime, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            )
        }
    ) { paddingValues ->

        AnimatedVisibility(state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            uiState = state.currentWeather,
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier, uiState: CurrentWeather) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono del clima y temperatura

        val iconUrl = uiState.weatherIcons
            .firstOrNull { it.isNotBlank() } // Filtra elementos vacíos o nulos
            ?: "URL_DE_IMAGEN_POR_DEFECTO"

        Box(
            modifier = Modifier
                .size(90.dp)
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = iconUrl,
                contentDescription = iconUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        val description = uiState.weatherDescriptions
            .firstOrNull { it.isNotBlank() } // Filtra elementos vacíos o nulos
            ?: "URL_DE_DESCRIPCION_POR_DEFECTO"

        Text("${uiState.temperature}°C", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        Text(description, fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(50.dp))

        // Detalles del clima
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                WeatherDetailRow("Temperatura", "${uiState.temperature}°C")
                WeatherDetailRow("Humedad", "${uiState.humidity}%")
                WeatherDetailRow("Viento", "${uiState.windSpeed} km/h")
                LazyColumn {
                    items(uiState.weatherDescriptions) { weatherDescription ->
                        WeatherDetailRow("Descripción del clima", weatherDescription)
                    }
                }
            }
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
        Text(label, fontSize = 16.sp)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DevExpertWeatherTheme {
        HomeScreen()
    }
}