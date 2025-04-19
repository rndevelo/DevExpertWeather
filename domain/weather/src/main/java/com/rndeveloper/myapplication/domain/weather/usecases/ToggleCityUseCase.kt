package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.weather.WeatherRepository

class ToggleCityUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: City, isFav: Boolean) {
        weatherRepository.toggleFavCity(city, isFav)
    }
}