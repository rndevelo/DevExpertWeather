package com.rndeveloper.myapplication.feature.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rndeveloper.myapplication.feature.common.theme.DevExpertWeatherTheme

@Composable
fun Screen(content: @Composable () -> Unit) {
    DevExpertWeatherTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}