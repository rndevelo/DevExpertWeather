package com.rndeveloper.myapplication.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rndeveloper.myapplication.feature.common.Forecast
import com.rndeveloper.myapplication.feature.common.Home
import com.rndeveloper.myapplication.feature.forecast.ForecastScreen
import com.rndeveloper.myapplication.feature.home.HomeScreen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                onForecastClick = { cityName: String, lat: String, long: String ->
                    navController.navigate(Forecast(cityName, lat, long))
                }
            )
        }
        composable<Forecast> { backStackEntry ->
            val forecast = backStackEntry.toRoute<Forecast>()
            ForecastScreen(onBack = { navController.popBackStack() })
        }
    }
}
