package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import com.rndeveloper.myapplication.domain.weather.model.Weather
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetWeatherUseCase(private val weatherRepository: WeatherRepository) {
    operator fun invoke(lat: Double, lon: Double): Flow<Weather> = weatherRepository.weather(lat, lon)
}