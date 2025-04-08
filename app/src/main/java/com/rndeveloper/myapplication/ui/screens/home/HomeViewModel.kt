package com.rndeveloper.myapplication.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.Result
import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.data.Weather
import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.data.datasource.remote.City
import com.rndeveloper.myapplication.stateAsResultIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeAction {
    data class OnSearchCities(val query: String) : HomeAction
    data object OnGetCityByLocation : HomeAction
    data class OnSelectedCity(val city: City) : HomeAction
    data class OnToggleCity(val city: City, val isFav: Boolean) : HomeAction
}

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val regionRepository: RegionRepository,
) : ViewModel() {

    // 🔁 Esto es controlable manualmente o por lógica de inicio
    private val _selectedCity = MutableStateFlow<City?>(null)
    val selectedCity: StateFlow<City?> = _selectedCity.asStateFlow()

    // 🔍 Ciudades buscadas (ahora accesible desde la UI)
    private val _searchedCities = MutableStateFlow<List<City>>(emptyList())
    val searchedCities: StateFlow<List<City>> = _searchedCities.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favCitiesState: StateFlow<Result<List<City>>> = weatherRepository.favCities.stateAsResultIn(viewModelScope)

    // 🌦 Estado del clima según la ciudad seleccionada
    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather?>> = selectedCity
        .filterNotNull()
        .flatMapLatest { city ->
            flow {
                val weather = weatherRepository.getWeather(city.latitude, city.longitude)
                emit(weather)
            }
        }
        .stateAsResultIn(viewModelScope)

    // 📌 Combina todo en un solo estado reactivo de la UI
    val uiState: StateFlow<UiState> = combine(
        weatherState,
        searchedCities,
        favCitiesState,
        selectedCity
    ) { weather, searchedCities, favCitiesResult, selectedCity ->
        UiState(
            weather = weather,
            searchedCities = searchedCities,
            favCities = favCitiesResult,
            selectedCity = selectedCity
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())


    data class UiState(
        val weather: Result<Weather?> = Result.Loading,
        val favCities: Result<List<City>> = Result.Loading,
        val searchedCities: List<City> = emptyList(),
        val selectedCity: City? = null,
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnGetCityByLocation -> onGetCityByLocation()
            is HomeAction.OnSearchCities -> onSearchCities(action.query)
            is HomeAction.OnSelectedCity -> _selectedCity.value = action.city
            is HomeAction.OnToggleCity -> onToggleFavCity(action.city, action.isFav)
        }
    }

    private fun onGetCityByLocation() = viewModelScope.launch {
        regionRepository.findLastLocationCityInfo().let { city ->
            _selectedCity.value =
                weatherRepository.searchCities(query = "${city.name}, ${city.country}")
                    .firstOrNull()
        }
    }

    private fun onSearchCities(query: String) = viewModelScope.launch {
        _searchedCities.value = weatherRepository.searchCities(query)
    }

    private fun onToggleFavCity(city: City, isFav: Boolean) = viewModelScope.launch {
        weatherRepository.toggleFavCity(city, isFav)
    }
}