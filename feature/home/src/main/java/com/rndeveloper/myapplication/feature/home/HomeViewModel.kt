package com.rndeveloper.myapplication.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetFromLocationCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.GetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.domain.location.usecases.SetSelectedCityUseCase
import com.rndeveloper.myapplication.domain.location.usecases.ToggleCityUseCase
import com.rndeveloper.myapplication.domain.weather.model.Weather
import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.feature.common.Result
import com.rndeveloper.myapplication.feature.common.stateAsResultIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeAction {
    data class OnSearchCities(val query: String) : HomeAction
    data object OnGetCityByLocation : HomeAction
    data class OnSelectedCity(val city: City) : HomeAction
    data class OnToggleCity(val city: City, val isFav: Boolean) : HomeAction
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    getSelectedCityUseCase: GetSelectedCityUseCase,
    private val setSelectedCityUseCase: SetSelectedCityUseCase,
    getFavCitiesUseCase: GetFavCitiesUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val toggleCityUseCase: ToggleCityUseCase,
    private val getFromLocationCityUseCase: GetFromLocationCityUseCase
) : ViewModel() {

    // 🔍 Ciudades buscadas (ahora accesible desde la UI)
    private val _searchedCitiesState = MutableStateFlow<List<City>>(emptyList())
    val searchedCitiesState: StateFlow<List<City>> = _searchedCitiesState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val favCitiesState: StateFlow<Result<List<City>>> =
        getFavCitiesUseCase().stateAsResultIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedCityState: StateFlow<Result<City?>> =
        getSelectedCityUseCase().stateAsResultIn(viewModelScope)


    // 🌦 Estado del clima según la ciudad seleccionada
    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherState: StateFlow<Result<Weather>> = selectedCityState
        .filterIsInstance<Result.Success<City>>() // solo sigue si hay City exitosamente cargada
        .map { it.data }
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
    ) { weatherResult, searchedCities, favCitiesResult, selectedCityResult ->

        val isLoading = weatherResult is Result.Loading || favCitiesResult is Result.Loading

        UiState(
            isLoading = isLoading,
            weatherResult = weatherResult,
            searchedCities = searchedCities,
            favCities = when (favCitiesResult) {
                is Result.Success -> favCitiesResult.data
                else -> emptyList()
            },
            selectedCity = when (selectedCityResult) {
                is Result.Success -> selectedCityResult.data
                else -> null
            },
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
                getFromLocationCityUseCase()?.let { city ->

                    val searchedCities =
                        searchCitiesUseCase(query = "${city.name}, ${city.country}").first()

                    setSelectedCityUseCase(searchedCities)

                }
            }

            is HomeAction.OnSearchCities -> viewModelScope.launch {
                _searchedCitiesState.value = searchCitiesUseCase(action.query)
            }

            is HomeAction.OnSelectedCity -> viewModelScope.launch {
                setSelectedCityUseCase(action.city)
            }
            is HomeAction.OnToggleCity -> viewModelScope.launch {
                toggleCityUseCase(action.city, action.isFav)
            }
        }
    }
}