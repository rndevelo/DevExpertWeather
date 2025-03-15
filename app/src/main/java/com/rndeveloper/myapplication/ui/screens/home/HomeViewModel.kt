package com.rndeveloper.myapplication.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.data.WeatherRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    private val repository = WeatherRepository()

    fun onUiReady(locality: String) {

        viewModelScope.launch {
            state = UiState(loading = true)
            state = UiState(loading = false, currentWeather = repository.getWeather(locality))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val currentWeather: CurrentWeather = CurrentWeather(
            cityName = "",
            country = "",
            localTime = "",
            temperature = 0.0,
            humidity = 0,
            windSpeed = 0.0,
            weatherDescriptions = emptyList(),
            weatherIcons = emptyList(),
        )
    )
}