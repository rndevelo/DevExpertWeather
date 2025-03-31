package com.rndeveloper.myapplication.ui.screens.home

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

class HomeViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _state = MutableStateFlow(UiState())
    val state get(): StateFlow<UiState> = _state.asStateFlow()


    fun onUiReady(lat: Double, lon: Double) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            _state.update {
                it.copy(
                    loading = false,
                    currentWeather = repository
                        .getWeather(lat = lat, lon = lon)
                        .current
                )
            }
        }
    }

    fun onSearchCities(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            _state.update {
                it.copy(
                    loading = false,
                    citiesInfo = repository.searchCities(query = query)
                )
            }
        }
    }

    fun onSelectedCityInfo(
        cityInfo: CityInfo
    ) = viewModelScope.launch {
        _state.update { it.copy(loading = true) }
        _state.update {
            it.copy(
                loading = false,
                selectedCityInfo = cityInfo
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