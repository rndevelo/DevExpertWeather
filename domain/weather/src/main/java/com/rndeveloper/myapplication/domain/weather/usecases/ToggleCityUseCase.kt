package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import javax.inject.Inject

class ToggleCityUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(city: City, isFav: Boolean) {
        weatherRepository.toggleFavCity(city, isFav)
    }
}