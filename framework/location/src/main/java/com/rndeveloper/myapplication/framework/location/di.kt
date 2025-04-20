package com.rndeveloper.myapplication.framework.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.rndeveloper.myapplication.data.location.LocationDataSource
import com.rndeveloper.myapplication.data.location.RegionDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FrameworkLocationBindsModule {

    @Binds
    abstract fun bindLocationDataSource(impl: PlayServicesLocationDataSource): LocationDataSource

    @Binds
    abstract fun bindRegionDataSource(impl: GeocoderRegionDataSource): RegionDataSource

}

@Module
@InstallIn(SingletonComponent::class)
internal class FrameworkLocationModule {

    @Provides
    fun provideFusedLocationProviderClient(context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideGeocoder(context: Context) = Geocoder(context)
}
