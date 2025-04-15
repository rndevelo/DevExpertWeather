package com.rndeveloper.myapplication.weather.usecases

import com.rndeveloper.myapplication.common.City
import com.rndeveloper.myapplication.weather.WeatherRepository
import kotlinx.coroutines.flow.Flow

class GetFavCitiesUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(): Flow<List<City>> = weatherRepository.favCities
}