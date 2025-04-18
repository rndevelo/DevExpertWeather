package com.rndeveloper.myapplication.weather.usecases

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.WeatherRepository

class ToggleCityUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: City, isFav: Boolean) {
        weatherRepository.toggleFavCity(city, isFav)
    }
}