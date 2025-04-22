package com.rndeveloper.myapplication.feature.forecast.di

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ForecastViewModelModule {

    @Provides
    @ViewModelScoped
    @CityName
    fun provideCityName(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle["cityName"] ?: throw IllegalArgumentException("City name is required")
    }

    @Provides
    @ViewModelScoped
    @Latitude
    fun provideLatitude(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle["lat"] ?: throw IllegalArgumentException("Latitude is required")
    }

    @Provides
    @ViewModelScoped
    @Longitude
    fun provideLongitude(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle["lon"] ?: throw IllegalArgumentException("Longitude name is required")
    }
}
