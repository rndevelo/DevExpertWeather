package com.rndeveloper.myapplication.ui.screens.home

import android.Manifest
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rndeveloper.myapplication.common.PermissionRequestEffect
import com.rndeveloper.myapplication.common.getLocation
import com.rndeveloper.myapplication.common.getRegion
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.ui.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.components.TopAppBar
import com.rndeveloper.myapplication.ui.theme.DevExpertWeatherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: HomeViewModel = viewModel(), onForecastClick: (String, String, String) -> Unit) {

    val ctx = LocalContext.current.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val state by vm.state.collectAsState()
    val currentWeather = state.currentWeather

    var cityName by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var lon by remember { mutableStateOf("") }

    PermissionRequestEffect(permission = Manifest.permission.ACCESS_COARSE_LOCATION) { granted ->
        coroutineScope.launch {
            if (granted) {
                ctx.getLocation().let { location ->
                    lat = location?.latitude.toString()
                    lon = location?.longitude.toString()

                    vm.onUiReady(
                        lat = location?.latitude ?: 40.71,
                        lon = location?.longitude ?: 0.0
                    )
                }
                ctx.getRegion().let { location ->
                    cityName = ctx.getRegion()
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
                    TopAppBar(title = cityName, subtitle = currentWeather?.date ?: "")
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Pronóstico") },
                icon = { Text("\uD83D\uDCCA") },
                onClick = { onForecastClick(cityName, lat, lon) },
            )
        }
    ) { paddingValues ->

        LoadingAnimation(
            isLoading = state.loading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

        HomeContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            currentWeather = currentWeather,
        )
    }
}

@Composable
fun HomeContent(modifier: Modifier = Modifier, currentWeather: CurrentWeather?) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = currentWeather?.weatherIcon ?: "", fontSize = 60.sp,
            modifier = Modifier
                .size(90.dp)
                .padding(10.dp),
        )

        Text("${currentWeather?.temperature}°C", fontSize = 50.sp, fontWeight = FontWeight.Bold)
        Text(text = currentWeather?.weatherDescription ?: "", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(50.dp))

        // Detalles del clima
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                WeatherDetailRow("🌡️ Temperatura", "${currentWeather?.temperature}°C")
                WeatherDetailRow("💧 Humedad", "${currentWeather?.humidity}%")
                WeatherDetailRow("\uD83D\uDCA8 Viento", "${currentWeather?.windSpeed} km/h")
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
//        HomeScreen { -> a, b, c }
    }
}