package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.CityRepository
import javax.inject.Inject

class SetSelectedCityUseCase @Inject constructor(private val cityRepository: CityRepository) {
    suspend operator fun invoke(city: City) = cityRepository.insertSelectedCity(city)
}