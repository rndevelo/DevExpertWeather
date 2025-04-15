package com.rndeveloper.myapplication.usecases

import com.rndeveloper.myapplication.data.WeatherRepository

class SearchCitiesUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(query: String) = weatherRepository.searchCities(query)
}