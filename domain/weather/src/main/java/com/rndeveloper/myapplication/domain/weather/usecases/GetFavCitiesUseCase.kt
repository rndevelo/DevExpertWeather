package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.common.City
import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetFavCitiesUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke(): Flow<List<City>> = weatherRepository.favCities
}