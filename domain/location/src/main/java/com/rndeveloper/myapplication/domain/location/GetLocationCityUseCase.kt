package com.rndeveloper.myapplication.domain.location

import com.rndeveloper.myapplication.domain.common.City

class GetLocationCityUseCase(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.findLastLocationCityInfo()
}