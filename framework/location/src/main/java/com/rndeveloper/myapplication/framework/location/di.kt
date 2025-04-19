package com.rndeveloper.myapplication.framework.location

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.LocationServices
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan
class FrameworkLocationModule

val frameworkLocationModule = module {
    factory { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    factory { Geocoder(get<Context>()) }
}