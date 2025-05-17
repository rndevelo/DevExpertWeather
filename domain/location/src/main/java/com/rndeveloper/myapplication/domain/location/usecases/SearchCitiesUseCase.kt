package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.CityRepository
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend operator fun invoke(query: String) = cityRepository.searchCities(query)
}