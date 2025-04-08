package com.rndeveloper.myapplication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rndeveloper.myapplication.Result
import com.rndeveloper.myapplication.ui.screens.components.LoadingAnimation
import com.rndeveloper.myapplication.ui.screens.home.components.FavCitiesContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}

inline fun<T> Result<T>.ifSuccess(block: (T) -> Unit) {
    if (this is Result.Success) block(data)
}

fun <T> Flow<T>.stateAsResultIn(scope: CoroutineScope): StateFlow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .catch { emit(Result.Error(it)) }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Result.Loading
        )

@Composable
fun <T> Result<T>.ShowResult(content: @Composable () -> Unit){
    when (this) {
        is Result.Loading -> LoadingAnimation(modifier = Modifier.fillMaxSize())
        is Result.Error -> Text(text = "Error: ${exception.message}")
        is Result.Success -> content()
    }
}