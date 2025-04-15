package com.rndeveloper.myapplication.usecases

import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.domain.City

class GetLocationCityUseCase(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.findLastLocationCityInfo()
}