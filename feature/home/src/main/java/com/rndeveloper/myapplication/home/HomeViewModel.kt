package com.rndeveloper.myapplication.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.common.Result
import com.rndeveloper.myapplication.common.stateAsResultIn
import com.rndeveloper.myapplication.location.GetLocationCityUseCase
import com.rndeveloper.myapplication.weather.model.Weather
import com.rndeveloper.myapplication.weather.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.weather.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.weather.usecases.ToggleCityUseCase
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
    private val getWeatherUseCase: GetWeatherUseCase,
    getFavCitiesUseCase: GetFavCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleCityUseCase: ToggleCityUseCase,
    private val getLocalCityUseCase: GetLocationCityUseCase,
) : ViewModel() {

    // 🔁 Esto es controlable manualmente o por lógica de inicio
    private val _selectedCityState = MutableStateFlow<City?>(null)
    val selectedCityState: StateFlow<City?> = _selectedCityState.asStateFlow()

    // 🔍 Ciudades buscadas (ahora accesible desde la UI)
    private val _searchedCitiesState = MutableStateFlow<List<City>>(emptyList())
    val searchedCitiesState: StateFlow<List<City>> = _searchedCitiesState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favCitiesState: StateFlow<Result<List<City>>> =
        getFavCitiesUseCase().stateAsResultIn(viewModelScope)

    // 🌦 Estado del clima según la ciudad seleccionada
    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> = selectedCityState
        .filterNotNull()
        .flatMapLatest { city ->
            getWeatherUseCase(city.lat, city.lon)
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
        val weatherResult: Result<Weather> = Result.Loading,
        val favCities: List<City> = emptyList(),
        val searchedCities: List<City> = emptyList(),
        val selectedCity: City? = null,
    )

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnGetCityByLocation -> viewModelScope.launch {
                getLocalCityUseCase()?.let { city ->
                    _selectedCityState.value =
                        searchCitiesUseCase(query = "${city.name}, ${city.country}")
                            .firstOrNull()
                }
            }

            is HomeAction.OnSearchCities -> viewModelScope.launch {
                _searchedCitiesState.value = searchCitiesUseCase(action.query)
            }

            is HomeAction.OnSelectedCity -> _selectedCityState.value = action.city
            is HomeAction.OnToggleCity -> viewModelScope.launch {
                toggleCityUseCase(action.city, action.isFav)
            }
        }
    }
}