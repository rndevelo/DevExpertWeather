package com.rndeveloper.myapplication

import android.app.Application
import com.rndeveloper.myapplication.data.location.DataLocationModule
import com.rndeveloper.myapplication.data.weather.DataWeatherModule
import com.rndeveloper.myapplication.domain.location.DomainLocationModule
import com.rndeveloper.myapplication.domain.weather.DomainWeatherModule
import com.rndeveloper.myapplication.feature.forecast.FeatureForecastModule
import com.rndeveloper.myapplication.feature.home.FeatureHomeModule
import com.rndeveloper.myapplication.framework.location.FrameworkLocationModule
import com.rndeveloper.myapplication.framework.location.frameworkLocationModule
import com.rndeveloper.myapplication.framework.weather.FrameworkWeatherModule
import com.rndeveloper.myapplication.framework.weather.frameworkWeatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                frameworkLocationModule,
                frameworkWeatherModule,
                FrameworkLocationModule().module,
                FrameworkWeatherModule().module,
                FeatureHomeModule().module,
                FeatureForecastModule().module,
                DataLocationModule().module,
                DataWeatherModule().module,
                DomainLocationModule().module,
                DomainWeatherModule().module,
            )
        }
    }
}
