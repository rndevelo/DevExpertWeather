package com.rndeveloper.myapplication.feature.forecast

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Named
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @Named("cityName")
    fun providerCity(savedStateHandle: SavedStateHandle): String {
        return savedStateHandle[]

    }
}