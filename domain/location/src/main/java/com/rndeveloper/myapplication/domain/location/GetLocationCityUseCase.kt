package com.rndeveloper.myapplication.domain.location

import com.rndeveloper.myapplication.domain.common.City
import javax.inject.Inject

class GetLocationCityUseCase @Inject constructor(private val regionRepository: RegionRepository) {
    suspend operator fun invoke(): City? = regionRepository.findLastLocationCityInfo()
}
