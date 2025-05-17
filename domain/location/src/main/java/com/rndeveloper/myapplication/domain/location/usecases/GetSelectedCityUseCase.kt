package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedCityUseCase @Inject constructor(private val cityRepository: CityRepository) {
    operator fun invoke(): Flow<City> = cityRepository.selectedCity
}