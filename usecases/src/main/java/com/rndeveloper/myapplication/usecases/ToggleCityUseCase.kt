package com.rndeveloper.myapplication.usecases

import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.domain.City

class ToggleCityUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: City, isFav: Boolean) {
        weatherRepository.toggleFavCity(city, isFav)
    }
}