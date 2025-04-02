package com.rndeveloper.myapplication.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rndeveloper.myapplication.data.CurrentWeather
import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.data.datasource.remote.City
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
    data class OnToggleCity(val city: City, val isFav: Boolean) : HomeAction
}

class HomeViewModel(
    private val weatherRepository: WeatherRepository,
    private val regionRepository: RegionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state get(): StateFlow<UiState> = _state.asStateFlow()

//    val favCitiesState: StateFlow<Result<List<City>>> =
//        weatherRepository.favCities
//            .stateAsResultIn(viewModelScope)


    data class UiState(
        val loading: Boolean = false,
        val currentWeather: CurrentWeather? = null,
        val searchedCities: List<City> = emptyList(),
        val favCities: List<City> = emptyList(),
        val selectedCity: City? = null,
    )

    init {
        onAction(HomeAction.OnGetFavCities)
    }

//    private val currentWeatherFlow = MutableStateFlow<CurrentWeather?>(null)
//    private val searchedCitiesFlow = MutableStateFlow<List<City>>(emptyList())
//    private val selectedCityFlow = MutableStateFlow<City?>(null)
//
//    // 🔥 **Combinar todos los estados en un único `UiState`**
//    val state: StateFlow<Result<UiState>> = combine(
//        weatherRepository.favCities,
//        weatherRepository.getWeather(),
//        weatherRepository.searchCities(""),
//        selectedCityFlow
//    ) { favCities, currentWeather, searchedCities, selectedCity ->
//        UiState(
//            favCities = favCities,
//            currentWeather = currentWeather,
//            searchedCities = searchedCities,
//            selectedCity = selectedCity
//        )
//    }.stateAsResultIn(viewModelScope)

    fun onAction(action: HomeAction) {
        _state.update { it.copy(loading = true) }
        when (action) {
            is HomeAction.OnGetWeather -> onGetWeather(action.lat, action.lon)
            is HomeAction.OnSearchCities -> onSearchCities(action.query)
            is HomeAction.OnGetCityByLocation -> onGetCityByLocation()
            is HomeAction.OnSelectedCity -> _state.update {
                it.copy(
                    loading = false,
                    selectedCity = action.city
                )
            }

            is HomeAction.OnGetFavCities -> onGetFavCities()
            is HomeAction.OnToggleCity -> onToggleFavCity(action.city, action.isFav)
        }
    }

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

    private fun onGetCityByLocation() = viewModelScope.launch {
        regionRepository.findLastLocationCityInfo().let { city ->
            _state.update {
                it.copy(
                    loading = false,
                    selectedCity = weatherRepository.searchCities(query = "${city.name}, ${city.country}")
                        .firstOrNull()
                )
            }
        }
    }

    private fun onToggleFavCity(city: City, isFav: Boolean) = viewModelScope.launch {
        weatherRepository.toggleFavCity(city, isFav)
        _state.update {
            it.copy(loading = false)
        }
    }

    private fun onGetFavCities() = viewModelScope.launch {
        weatherRepository.favCities.collect { favCities ->
            _state.update {
                it.copy(
                    loading = false,
                    favCities = favCities
                )
            }
        }
    }
}