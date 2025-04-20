package com.rndeveloper.myapplication.feature.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.feature.common.stateAsResultIn
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val cityName: String,
    lat: String,
    lon: String,
    getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> =
        getWeatherUseCase(lat.toDouble(), lon.toDouble()).stateAsResultIn(viewModelScope)


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