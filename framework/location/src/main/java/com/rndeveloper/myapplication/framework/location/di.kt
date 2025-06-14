package com.rndeveloper.myapplication.framework.location

import android.app.Application
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.rndeveloper.myapplication.data.location.datasources.CityLocalDataSource
import com.rndeveloper.myapplication.data.location.datasources.CityRemoteDataSource
import com.rndeveloper.myapplication.data.location.datasources.LocationDataSource
import com.rndeveloper.myapplication.data.location.datasources.RegionDataSource
import com.rndeveloper.myapplication.framework.location.local.CityRoomDataSource
import com.rndeveloper.myapplication.framework.location.remote.CityServerDataSource
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

    @Binds
    abstract fun bindCityLocalDataSource(impl: CityRoomDataSource): CityLocalDataSource

    @Binds
    abstract fun bindCityRemoteDataSource(impl: CityServerDataSource): CityRemoteDataSource

}

@Module
@InstallIn(SingletonComponent::class)
internal class FrameworkLocationModule {

    @Provides
    fun provideFusedLocationProviderClient(app: Application) =
        LocationServices.getFusedLocationProviderClient(app)

    @Provides
    fun provideGeocoder(app: Application) = Geocoder(app)
}
