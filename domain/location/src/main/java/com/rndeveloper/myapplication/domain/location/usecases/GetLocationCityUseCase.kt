package com.rndeveloper.myapplication.domain.location.usecases

import com.rndeveloper.myapplication.domain.location.City
import com.rndeveloper.myapplication.domain.location.RegionRepository
import javax.inject.Inject

class GetLocationCityUseCase @Inject constructor(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.cityByLastLocation()
}