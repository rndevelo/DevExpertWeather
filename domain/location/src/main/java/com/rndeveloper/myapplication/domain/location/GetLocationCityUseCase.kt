package com.rndeveloper.myapplication.domain.location

import com.rndeveloper.myapplication.domain.common.City
import org.koin.core.annotation.Factory

@Factory
class GetLocationCityUseCase(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.findLastLocationCityInfo()
}