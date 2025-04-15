package com.rndeveloper.myapplication.ui.screens.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.Result
import com.rndeveloper.myapplication.stateAsResultIn
import com.rndeveloper.myapplication.weather.model.Weather
import com.rndeveloper.myapplication.weather.usecases.GetWeatherUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ForecastViewModel(
    private val cityName: String,
    lat: Double,
    lon: Double,
    getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> =
        getWeatherUseCase(lat, lon).stateAsResultIn(viewModelScope)


    // Combine del nombre + weather
    val uiState: StateFlow<UiState> = weatherState.map { weatherResult ->
        UiState(
            cityName = cityName,
            weather = weatherResult
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiState()
    )


    data class UiState(
        val cityName: String = "",
        val weather: Result<Weather> = Result.Loading,
    )
}