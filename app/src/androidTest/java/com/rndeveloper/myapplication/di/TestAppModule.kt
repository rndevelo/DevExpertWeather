package com.rndeveloper.myapplication.di

import android.app.Application
import androidx.room.Room
import com.rndeveloper.framework.core.AppDatabase
import com.rndeveloper.framework.core.FrameworkCoreExtrasModule
import com.rndeveloper.framework.core.GeocodingApiUrl
import com.rndeveloper.framework.core.WeatherApiUrl
import com.rndeveloper.myapplication.data.server.TestUrlProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FrameworkCoreExtrasModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        val db = Room.inMemoryDatabaseBuilder(
            app,
            AppDatabase::class.java
        )
            .setTransactionExecutor(Dispatchers.Main.asExecutor())
            .allowMainThreadQueries()
            .build()
        return db
    }

    @Provides
    @GeocodingApiUrl
    fun provideTestGeocodingApiUrl(): String = TestUrlProvider.mockUrl

    @Provides
    @WeatherApiUrl
    fun provideTestWeatherApiUrl(): String = TestUrlProvider.mockUrl
}