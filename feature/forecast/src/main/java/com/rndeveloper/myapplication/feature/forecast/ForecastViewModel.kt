package com.rndeveloper.myapplication.feature.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.feature.common.stateAsResultIn
import com.rndeveloper.myapplication.feature.forecast.di.CityName
import com.rndeveloper.myapplication.feature.forecast.di.Latitude
import com.rndeveloper.myapplication.feature.forecast.di.Longitude
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ForecastViewModel @Inject constructor(
    @CityName private val cityName: String,
    @Latitude lat: String,
    @Longitude lon: String,
    getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> =
        getWeatherUseCase(lat.toDouble(), lon.toDouble()).stateAsResultIn(viewModelScope)

    // Combine del nombre + weather
    val state: StateFlow<UiState> = weatherState.map { weatherResult ->
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
