package com.rndeveloper.myapplication.ui.screens.forecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.Result
import com.rndeveloper.myapplication.data.DailyForecast
import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.stateAsResultIn
import com.rndeveloper.myapplication.ui.screens.home.HomeViewModel
import com.rndeveloper.myapplication.ui.screens.home.HomeViewModel.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val cityName: String,
    private val lat: Double,
    private val lon: Double,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather?>> =
        flow {
            val weather = weatherRepository.getWeather(lat, lon)
            emit(weather)
        }.stateAsResultIn(viewModelScope)


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
        val weather: Result<Weather?> = Result.Loading,
    )
}