package com.rndeveloper.myapplication.usecases

import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.domain.City
import kotlinx.coroutines.flow.Flow

class GetFavCitiesUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(): Flow<List<City>> = weatherRepository.favCities
}