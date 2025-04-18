package com.rndeveloper.myapplication.weather.usecases

import com.rndeveloper.myapplication.weather.WeatherRepository


class SearchCitiesUseCase(private val weatherRepository: WeatherRepository) {
    suspend operator fun invoke(query: String) = weatherRepository.searchCities(query)
}