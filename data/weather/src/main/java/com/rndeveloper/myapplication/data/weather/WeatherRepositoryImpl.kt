package com.rndeveloper.myapplication.data.weather

import com.rndeveloper.myapplication.domain.weather.WeatherRepository
import com.rndeveloper.myapplication.domain.weather.model.Weather
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
) : WeatherRepository {


    override fun weather(lat: Double, lon: Double): Flow<Weather> = flow {
        val localWeather = weatherLocalDataSource.weather(lat, lon).firstOrNull()

        if (localWeather == null || shouldFetchRemote(localWeather.lastUpdated)) {
            val remoteWeather = weatherRemoteDataSource.getWeather(lat, lon)
            weatherLocalDataSource.insertWeather(remoteWeather)
        }

        emitAll(weatherLocalDataSource.weather(lat, lon).filterNotNull())
    }

//    override fun weather(lat: Double, lon: Double): Flow<Weather> {
//        // Necesitamos un CoroutineScope para lanzar la operación de red/guardado
//        // si el `onEach` lo requiere. El colector del Flow `weather`
//        // proporcionará este scope.
//        // Esta es una simplificación; en un ViewModel, usarías viewModelScope.
//        // En un repositorio, a menudo se pasa un scope o se usa un scope global
//        // para operaciones de "carga en segundo plano", pero aquí, la carga es
//        // parte de la solicitud del Flow.
//
//        return weatherLocalDataSource.weather // Este es el Flow base que observamos
//            .onStart {
//                // Este bloque se ejecuta cuando el Flow es recolectado por primera vez.
//                // Es un buen lugar para verificar el estado inicial y potencialmente
//                // desencadenar una carga remota ANTES de que el Flow base (weatherLocalDataSource.weather)
//                // emita su primer valor (o si emite null).
//
//                // Obtenemos el estado actual del caché una sola vez para decidir.
//                val currentCachedWeather = weatherLocalDataSource.weather.firstOrNull()
//
//                if (currentCachedWeather == null || shouldFetchRemote(currentCachedWeather.lastUpdated)) {
//                    // Si necesitamos obtener de remoto, lo hacemos aquí.
//                    // Esta llamada suspend se ejecutará en el CoroutineContext del colector.
//                    try {
//                        val remoteWeather = weatherRemoteDataSource.getWeather(lat, lon)
//                        weatherLocalDataSource.insertWeather(remoteWeather)
//                        // Después de esto, weatherLocalDataSource.weather (si es de Room)
//                        // debería emitir automáticamente el 'remoteWeather'.
//                    } catch (e: Exception) {
//                        // Si la carga remota falla Y no había nada en caché,
//                        // podríamos querer que el Flow falle o no emita nada.
//                        // Si ya había algo en caché (aunque obsoleto), el Flow
//                        // principal (weatherLocalDataSource.weather) aún podría emitirlo.
//                        if (currentCachedWeather == null) {
//                            // Podrías re-lanzar o manejar de otra forma.
//                            // Si se re-lanza aquí, el Flow consumidor recibirá el error.
//                            // Por ahora, lo dejaremos para que el Flow principal intente emitir.
//                            // Considera cómo quieres manejar errores de red aquí.
//                            println("Error fetching remote weather: ${e.message}")
//                        }
//                    }
//                }
//            }
//            .filterNotNull() // Nos aseguramos de solo emitir Weather no nulos.
//    }

    private fun shouldFetchRemote(lastUpdated: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val tenMinutesInMillis = 10 * 60 * 1000 // 10 minutos en milisegundos
        return (currentTime - lastUpdated) > tenMinutesInMillis
    }
}

