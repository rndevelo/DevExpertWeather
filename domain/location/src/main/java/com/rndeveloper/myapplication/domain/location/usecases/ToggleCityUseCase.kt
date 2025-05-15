package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.CityRepository
import javax.inject.Inject

class ToggleCityUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend operator fun invoke(city: City, isFav: Boolean) {
        cityRepository.toggleFavCity(city, isFav)
    }
}