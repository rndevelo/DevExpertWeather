package com.rndeveloper.myapplication.data.location

import com.rndeveloper.myapplication.domain.location.CityRepository
import com.rndeveloper.myapplication.domain.location.RegionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataLocationBindsModule {

    @Binds
    abstract fun bindRegionDataSource(impl: RegionRepositoryImpl): RegionRepository

    @Binds
    abstract fun bindLocationDataSource(impl: CityRepositoryImpl): CityRepository


}
