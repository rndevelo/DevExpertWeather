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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeAction {
    data class OnSearchCities(val query: String) : HomeAction
    data object OnGetCityByLocation : HomeAction
    data class OnSelectedCity(val city: City?) : HomeAction
    data class OnToggleCity(val city: City, val isFav: Boolean) : HomeAction
}

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val regionRepository: RegionRepository,
) : ViewModel() {

    // 🔁 Esto es controlable manualmente o por lógica de inicio
    private val _selectedCityState = MutableStateFlow<City?>(null)
    val selectedCityState: StateFlow<City?> = _selectedCityState.asStateFlow()

    // 🔍 Ciudades buscadas (ahora accesible desde la UI)
    private val _searchedCitiesState = MutableStateFlow<List<City>>(emptyList())
    val searchedCitiesState: StateFlow<List<City>> = _searchedCitiesState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favCitiesState: StateFlow<Result<List<City>>> =
        weatherRepository.favCities.stateAsResultIn(viewModelScope)

    // 🌦 Estado del clima según la ciudad seleccionada
    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> = selectedCityState
        .filterNotNull()
        .flatMapLatest { city ->
            weatherRepository.weather(city.latitude, city.longitude)
        }
        .stateAsResultIn(viewModelScope)

    // 📌 Combina todo en un solo estado reactivo de la UI
    val uiState: StateFlow<UiState> = combine(
        weatherState,
        searchedCitiesState,
        favCitiesState,
        selectedCityState
    ) { weatherResult, searchedCities, favCitiesResult, selectedCity ->

        val isLoading = weatherResult is Result.Loading || favCitiesResult is Result.Loading

        UiState(
            isLoading = isLoading,
            weatherResult = weatherResult,
            searchedCities = searchedCities,
            favCities = when (favCitiesResult) {
                is Result.Success -> favCitiesResult.data
                else -> emptyList()
            },
            selectedCity = selectedCity,

        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())


    data class UiState(
        val isLoading: Boolean = false,
        val weatherResult:  Result<Weather> = Result.Loading,
        val favCities: List<City> = emptyList(),
        val searchedCities: List<City> = emptyList(),
        val selectedCity: City? = null,
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnGetCityByLocation -> viewModelScope.launch {
                regionRepository.findLastLocationCityInfo().let { city ->
                    _selectedCityState.value =
                        weatherRepository.searchCities(query = "${city.name}, ${city.country}")
                            .firstOrNull()
                }
            }

            is HomeAction.OnSearchCities -> viewModelScope.launch {
                _searchedCitiesState.value = weatherRepository.searchCities(action.query)
            }

            is HomeAction.OnSelectedCity -> _selectedCityState.value = action.city
            is HomeAction.OnToggleCity -> viewModelScope.launch {
                weatherRepository.toggleFavCity(action.city, action.isFav)
            }
        }
    }
}