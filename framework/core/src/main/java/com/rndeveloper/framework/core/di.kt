package com.rndeveloper.framework.core

import android.app.Application
import androidx.room.Room
import com.rndeveloper.myapplication.framework.location.remote.CityService
import com.rndeveloper.myapplication.framework.weather.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object FrameworkCoreModule {

    @Provides
    @Singleton
    fun provideWeatherDao(db: AppDatabase) = db.weatherDao()

    @Provides
    @Singleton
    fun provideFavCitiesDao(db: AppDatabase) = db.favCitiesDao()

    @Provides
    @Singleton
    fun provideSelectedCityDao(db: AppDatabase) = db.selectedCityDao()


    // Configuración común de Json para kotlinx.serialization
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        // otras configuraciones si las necesitas
    }

    @Provides
    @Singleton
    fun provideWeatherService(
        @WeatherApiUrl weatherApiUrl: String, // Inyecta la URL cualificada
        json: Json // Inyecta la instancia de Json
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl(weatherApiUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideCityService(
        @GeocodingApiUrl geocodingApiUrl: String, // Inyecta la URL cualificada
        json: Json // Inyecta la instancia de Json
    ): CityService {
        return Retrofit.Builder()
            .baseUrl(geocodingApiUrl)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CityService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FrameworkCoreExtrasModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, "app-db").build()

    @Provides
    @Singleton
    @WeatherApiUrl // Usa tu cualificador
    fun provideWeatherApiUrl(): String = "https://api.open-meteo.com/v1/"

    @Provides
    @Singleton
    @GeocodingApiUrl // Usa tu cualificador
    fun provideGeocodingApiUrl(): String = "https://geocoding-api.open-meteo.com/v1/"
}
