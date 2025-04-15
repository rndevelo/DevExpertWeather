package com.rndeveloper.myapplication.location

import com.rndeveloper.myapplication.common.City

class GetLocationCityUseCase(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.findLastLocationCityInfo()
}