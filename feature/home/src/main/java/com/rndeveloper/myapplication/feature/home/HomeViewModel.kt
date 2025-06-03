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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeAction {
    data class OnSearchCities(val query: String) : HomeAction
    data object OnGetCityFromGPSLocation : HomeAction
    data class OnSelectedCity(val city: City) : HomeAction
    data class OnToggleCity(val city: City, val isFav: Boolean) : HomeAction
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    getSelectedCityUseCase: GetSelectedCityUseCase,
    private val setSelectedCityUseCase: SetSelectedCityUseCase,
    getFavCitiesUseCase: GetFavCitiesUseCase,
    private val toggleCityUseCase: ToggleCityUseCase,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val getCityFromLocationGPSUseCase: GetFromLocationCityUseCase
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
        .mapNotNull { result -> // Usar mapNotNull para manejar errores y obtener solo Success con datos no nulos
            (result as? Result.Success<City?>)?.data // Extrae City si es Success y no nulo
        }
        .flatMapLatest { city -> // city aquí ya no será null
            getWeatherUseCase(city.lat, city.lon)
        }
        .stateAsResultIn(viewModelScope)

    // 📌 Combina todo en un solo estado reactivo de la UI
    val state: StateFlow<UiState> = combine(
        weatherState,
        searchedCitiesState,
        favCitiesState,
        selectedCityState
    ) { weatherResult, searchedCities, favCitiesResult, selectedCityResult ->

        UiState(
            selectedCity = selectedCityResult,
            weatherResult = weatherResult,
            searchedCities = searchedCities,
            favCities = when (favCitiesResult) {
                is Result.Success -> favCitiesResult.data
                else -> emptyList()
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())


    data class UiState(
        val selectedCity: Result<City?> = Result.Loading,
        val weatherResult: Result<Weather> = Result.Loading,
        val searchedCities: List<City> = emptyList(),
        val favCities: List<City> = emptyList(),
    )

    fun onAction(action: HomeAction) = viewModelScope.launch {
        when (action) {
            is HomeAction.OnGetCityFromGPSLocation -> {
                getCityFromLocationGPSUseCase()?.let { city ->
                    val results = searchCitiesUseCase("${city.name}, ${city.country}")
                    val searchedCity = results.first()
                    setSelectedCityUseCase(searchedCity)
                }
            }

            is HomeAction.OnSearchCities -> {
                _searchedCitiesState.value = searchCitiesUseCase(action.query)
            }

            is HomeAction.OnSelectedCity -> {
                setSelectedCityUseCase(action.city)
            }

            is HomeAction.OnToggleCity -> {
                toggleCityUseCase(action.city, action.isFav)
            }
        }
    }
}