package com.rndeveloper.myapplication.usecases

import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.domain.Weather
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(lat: Double, lon: Double): Flow<Weather> = weatherRepository.weather(lat, lon)
}