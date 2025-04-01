package com.rndeveloper.myapplication.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.datasource.remote.City
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeAction {
    data class OnGetWeather(val lat: Double, val lon: Double) : HomeAction
    data class OnSearchCities(val query: String) : HomeAction
    data object OnGetCityByLocation : HomeAction
    data class OnSelectedCity(val city: City) : HomeAction
    data object OnGetFavCities : HomeAction
    data class OnSaveCity(val city: City) : HomeAction
}

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val regionRepository: RegionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state get(): StateFlow<UiState> = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAction(action: HomeAction) {
        _state.update { it.copy(loading = true) }
        when (action) {
            is HomeAction.OnGetWeather -> onGetWeather(action.lat, action.lon)
            is HomeAction.OnSearchCities -> onSearchCities(action.query)
            is HomeAction.OnGetCityByLocation -> onGetCityInfoByLocation()
            is HomeAction.OnSelectedCity -> _state.update {
                it.copy(
                    loading = false,
                    selectedCity = action.city
                )
            }

            is HomeAction.OnGetFavCities -> onGetFavCities()
            is HomeAction.OnSaveCity -> onSaveCity(action.city)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onGetWeather(lat: Double, lon: Double) = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                currentWeather = weatherRepository
                    .getWeather(lat = lat, lon = lon)
                    .current
            )
        }
    }

    private fun onSearchCities(query: String) = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                searchedCities = weatherRepository.searchCities(query = query)
            )
        }
    }

    private fun onGetCityInfoByLocation() = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                selectedCity = regionRepository.findLastLocationCityInfo()
            )
        }
    }

    private fun onSaveCity(city: City) = viewModelScope.launch {
        weatherRepository.insertCity(city)
        _state.update {
            it.copy(loading = false)
        }
    }

    private fun onGetFavCities() = viewModelScope.launch {
        _state.update {
            it.copy(
                loading = false,
                favCities = weatherRepository.getFavCities()
            )
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val currentWeather: CurrentWeather? = null,
        val searchedCities: List<City> = emptyList(),
        val favCities: List<City> = emptyList(),
        val selectedCity: City? = null,
    )
}