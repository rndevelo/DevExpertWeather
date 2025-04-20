package com.rndeveloper.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rndeveloper.myapplication.feature.common.theme.DevExpertWeatherTheme
import com.rndeveloper.myapplication.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevExpertWeatherTheme {
                Navigation()
            }
        }
    }
}
