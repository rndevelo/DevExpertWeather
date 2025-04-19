package com.rndeveloper.myapplication.framework.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import com.rndeveloper.myapplication.data.location.LocationDataSource
import com.rndeveloper.myapplication.data.location.RegionDataSource
import com.rndeveloper.myapplication.framework.location.PlayServicesLocationDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val frameworkLocationModule = module {
    factoryOf(::PlayServicesLocationDataSource) bind LocationDataSource::class
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    factoryOf(::GeocoderRegionDataSource) bind RegionDataSource::class
    factory { Geocoder(get<Context>()) }
}