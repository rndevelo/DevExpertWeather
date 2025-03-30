package com.rndeveloper.myapplication.ui.screens.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.DailyForecast
import com.rndeveloper.myapplication.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val cityName: String,
    private val lat: Double,
    private val lon: Double
) : ViewModel() {

    private val repository = WeatherRepository()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady() {
        viewModelScope.launch {
            _state.value = UiState(
                loading = true,
                cityName = cityName
            )
            _state.value = UiState(
                loading = false,
                cityName = cityName,
                weatherForecast = repository.getWeather(lat = lat, lon = lon).forecast
            )
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val cityName: String = "",
        val weatherForecast: List<DailyForecast> = emptyList()
    )
}