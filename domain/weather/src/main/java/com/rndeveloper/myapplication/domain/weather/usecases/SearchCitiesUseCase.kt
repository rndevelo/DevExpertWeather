package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.weather.WeatherRepository


class SearchCitiesUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(query: String) = weatherRepository.searchCities(query)
}