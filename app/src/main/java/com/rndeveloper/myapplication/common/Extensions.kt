package com.rndeveloper.myapplication.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDataDate(): String {
    return try {
        // Intenta parsear como LocalDateTime (fecha y hora)
        val inputFormatterWithTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        val localDateTime = LocalDateTime.parse(this, inputFormatterWithTime)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        localDateTime.format(outputFormatter)
    } catch (_: Exception) {
        // Si falla, intenta parsear solo la fecha
        val inputFormatterWithoutTime = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(this, inputFormatterWithoutTime)
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        localDate.format(outputFormatter)
    }
}

//    Me obliga a poner esta anotación porque es la mínima versión que soporta el código
@RequiresApi(Build.VERSION_CODES.O)
fun String.formatUiDate(): String {
    val date = LocalDate.parse(this.formatDataDate())
    val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    return date.format(formatter).replaceFirstChar { it.uppercaseChar() }
}

fun Int.getWeatherDescription(): String {
    return when (this) {
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

fun Int.getWeatherIcon(): String {
    return when (this) {
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