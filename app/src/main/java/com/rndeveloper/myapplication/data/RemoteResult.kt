package com.rndeveloper.myapplication.data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class RemoteWeather(
    val current: RemoteCurrentWeather,
    val daily: RemoteDailyForecast
)

@Serializable
data class RemoteCurrentWeather(
    @SerialName(value = "time") val date: String,
    @SerialName(value = "weathercode") val weatherCode: Int,
    @SerialName(value = "temperature_2m") val temperature: Double,
    @SerialName(value = "relative_humidity_2m") val humidity: Int,
    @SerialName(value = "windspeed_10m") val windSpeed: Double,
    @SerialName(value = "precipitation") val precipitation: Double,
) {
    // Convertimos los datos en un objeto CurrentWeather para nuestro dominio
    @RequiresApi(Build.VERSION_CODES.O)
    fun toCurrentWeather(): CurrentWeather {
        return CurrentWeather(
            date = formatUiDate(date),
            weatherDescription = getWeatherDescription(weatherCode),
            weatherIcon = getWeatherIcon(weatherCode),
            temperature = temperature,
            humidity = humidity,
            windSpeed = windSpeed,
            precipitation = precipitation,
        )
    }
}

@Serializable
data class RemoteDailyForecast(
    @SerialName(value = "time") val dates: List<String>,
    @SerialName(value = "temperature_2m_max") val maxTemperatures: List<Double>,
    @SerialName(value = "temperature_2m_min") val minTemperatures: List<Double>,
    @SerialName(value = "precipitation_sum") val precipitationSum: List<Double>,
    @SerialName(value = "weathercode") val weatherCodes: List<Int>
) {
    // Convertimos los datos en una lista de objetos DailyForecast para nuestro dominio
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDailyForecastList(): List<DailyForecast> {
        return dates.mapIndexed { index, date ->
            DailyForecast(
                date = formatUiDate(date),
                weatherDescription = getWeatherDescription(weatherCodes.getOrNull(index)!!),
                weatherIcon = getWeatherIcon(weatherCodes.getOrNull(index)!!),
                maxTemperature = maxTemperatures.getOrNull(index) ?: 0.0,
                minTemperature = minTemperatures.getOrNull(index) ?: 0.0,
                precipitation = precipitationSum.getOrNull(index) ?: 0.0,
            )
        }
    }
}


data class Weather(
    val current: CurrentWeather,
    val forecast: List<DailyForecast>
)


data class CurrentWeather(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitation: Double,
)


// Nueva clase para representar el pronóstico diario de forma individual
data class DailyForecast(
    val date: String,
    val weatherDescription: String,
    val weatherIcon: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val precipitation: Double,
)

@RequiresApi(Build.VERSION_CODES.O)
fun formatDataDate(input: String): String {
    return try {
        // Intenta parsear como LocalDateTime (fecha y hora)
        val inputFormatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val localDateTime = LocalDateTime.parse(input, inputFormatterWithTime)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        localDateTime.format(outputFormatter)
    } catch (e: Exception) {
        // Si falla, intenta parsear solo la fecha
        val inputFormatterWithoutTime = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(input, inputFormatterWithoutTime)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        localDate.format(outputFormatter)
    }
}

//    Me obliga a poner esta anotación porque es la mínima versión que soporta el código
@RequiresApi(Build.VERSION_CODES.O)
fun formatUiDate(apiDate: String): String {
    val date = LocalDate.parse(formatDataDate(apiDate))
    val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return date.format(formatter).replaceFirstChar { it.uppercaseChar() }
}

fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "Despejado"
        in 1..3 -> "Parcialmente nublado"
        in 45..48 -> "Niebla"
        in 51..55 -> "Llovizna"
        in 61..67 -> "Lluvia"
        in 71..77 -> "Nieve"
        in 80..82 -> "Lluvias fuertes"
        in 95..99 -> "Tormentas"
        else -> "Desconocido"
    }
}

fun getWeatherIcon(code: Int): String {
    return when (code) {
        0 -> "☀️"
        in 1..3 -> "⛅"
        in 45..48 -> "🌫"
        in 51..55 -> "🌧"
        in 61..67 -> "🌧"
        in 71..77 -> "❄️"
        in 80..82 -> "🌧"
        in 95..99 -> "⛈"
        else -> "❓"
    }
}