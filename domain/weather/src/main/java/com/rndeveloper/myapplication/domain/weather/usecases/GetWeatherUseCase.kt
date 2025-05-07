package com.rndeveloper.myapplication.domain.weather.usecases

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import com.rndeveloper.myapplication.domain.weather.model.Weather
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    operator fun invoke(lat: Double, lon: Double): Flow<Weather> =
        weatherRepository.weather(lat, lon)
}