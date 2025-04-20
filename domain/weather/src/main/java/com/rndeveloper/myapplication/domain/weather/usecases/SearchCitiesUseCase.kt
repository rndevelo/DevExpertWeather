package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import jakarta.inject.Inject

class SearchCitiesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(query: String) = weatherRepository.searchCities(query)
}