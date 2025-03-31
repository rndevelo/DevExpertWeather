package com.rndeveloper.myapplication.data

import com.rndeveloper.myapplication.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

object WeatherClient {

//    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor) // <-- Agregar el interceptor aquí
//        .addInterceptor { apiKeyAsQuery(it) }
//        .build()

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val instance = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
//        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create<WeatherService>()

    private fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
        chain
            .request()
            .newBuilder()
            .url(
                chain.request().url
                    .newBuilder()
                    .addQueryParameter("appid", BuildConfig.WS_API_KEY)
                    .build()
            ).build()
    )
}
