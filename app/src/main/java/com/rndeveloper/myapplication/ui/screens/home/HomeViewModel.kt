package com.rndeveloper.myapplication.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _state = MutableStateFlow(UiState())
    val state get(): StateFlow<UiState> = _state.asStateFlow()


    fun onUiReady(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(
                loading = false,
                currentWeather = repository
                    .getWeather(lat = lat, lon = lon)
                    .current
            )

            Log.d("HomeViewModel", "onUiReady: hourly ${repository.getWeather(lat = lat, lon = lon).current.temperature}")
            Log.d("HomeViewModel", "onUiReady: forecast ${repository.getWeather(lat = lat, lon = lon).forecast.take(3)}")
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val currentWeather: CurrentWeather? = null,
    )
}