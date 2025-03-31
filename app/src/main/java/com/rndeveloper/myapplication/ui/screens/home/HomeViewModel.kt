package com.rndeveloper.myapplication.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.CityInfo
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeAction {
    data class OnUiReady(val lat: Double, val lon: Double) : HomeAction
    data class OnSearchCities(val query: String) : HomeAction
    data class OnSelectedCityInfo(val cityInfo: CityInfo) : HomeAction
}

class HomeViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _state = MutableStateFlow(UiState())
    val state get(): StateFlow<UiState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAction(action: HomeAction) {
        _state.update { it.copy(loading = true) }
        when (action) {
            is HomeAction.OnUiReady -> onUiReady(action.lat, action.lon)
            is HomeAction.OnSearchCities -> onSearchCities(action.query)
            is HomeAction.OnSelectedCityInfo -> _state.update {
                it.copy(
                    loading = false,
                    selectedCityInfo = action.cityInfo
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onUiReady(lat: Double, lon: Double) = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                currentWeather = repository
                    .getWeather(lat = lat, lon = lon)
                    .current
            )
        }
    }

    private fun onSearchCities(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                citiesInfo = repository.searchCities(query = query)
            )
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val currentWeather: CurrentWeather? = null,
        val citiesInfo: List<CityInfo> = emptyList(),
        val selectedCityInfo: CityInfo? = null,
    )
}