package com.rndeveloper.myapplication.ui.screens.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rndeveloper.myapplication.data.DailyForecast
import com.rndeveloper.myapplication.ui.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.components.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    vm: ForecastViewModel,
    onBack: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.onUiReady()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBar(
                        title = state.cityName,
                        subtitle = "Pronóstico de 7 días"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = Icons.Default.ArrowBack.toString()
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->

        LoadingAnimation(
            isLoading = state.loading,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.weatherForecast.isNotEmpty()) {
                items(state.weatherForecast) { forecast ->
                    WeatherForecastItem(forecast)
                }
            }
        }
    }

//    LazyColumn {
//        item {
//            Text(text = "📍 Ubicación")
//            Text(text = "🌡️ Temp. actual")
//            Text(text = "💧 Humedad")
//            Text(text = "🌧️ Precipitación")
//            Text(text = "🔽 Pronóstico de los próximos 7 días:")
//        }
//
//        items(state.weatherForecast) { forecast ->
//            Text(
//                text = "📅 ${forecast.date} → Max: ${forecast.maxTemperature}°C, Min: ${forecast.minTemperature}°C, 🌧️ Precipitación: ${forecast.precipitation}mm"
//            )
//        }
//    }

}

@Composable
fun WeatherForecastItem(forecast: DailyForecast) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = forecast.date,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = forecast.weatherDescription,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(text = forecast.weatherIcon, fontSize = 35.sp)
                Column {
                    Text(
                        text = "🌡️ Max: ${forecast.maxTemperature}°C",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "🌡️ Min: ${forecast.minTemperature}°C",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}