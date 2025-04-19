package com.rndeveloper.myapplication

import android.app.Application
import com.rndeveloper.myapplication.feature.forecast.featureForecastModule
import com.rndeveloper.myapplication.feature.home.featureHomeModule
import com.rndeveloper.myapplication.data.location.dataLocationModule
import com.rndeveloper.myapplication.domain.location.domainLocationModule
import com.rndeveloper.myapplication.framework.location.frameworkLocationModule
import com.rndeveloper.myapplication.data.weather.dataWeatherModule
import com.rndeveloper.myapplication.domain.weather.domainWeatherModule
import com.rndeveloper.myapplication.framework.weather.frameworkWeatherModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                frameworkLocationModule,
                frameworkWeatherModule,
                featureHomeModule,
                featureForecastModule,
                dataLocationModule,
                dataWeatherModule,
                domainLocationModule,
                domainWeatherModule,
            )
        }
    }
}
