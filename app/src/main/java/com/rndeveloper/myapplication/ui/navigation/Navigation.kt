package com.rndeveloper.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastScreen
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastViewModel
import com.rndeveloper.myapplication.ui.screens.home.HomeScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {

            HomeScreen { cityName: String, lat: String, long: String ->
                navController.navigate(NavScreen.Forecast.createRoute(cityName, lat, long))
            }
        }
        composable(
            route = NavScreen.Forecast.route,
            arguments = listOf(
                navArgument(NavArgs.CityName.key) { type = NavType.StringType },
                navArgument(NavArgs.Lat.key) { type = NavType.StringType },  // Double no es soportado, usa String
                navArgument(NavArgs.Long.key) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val cityName = requireNotNull(backStackEntry.arguments?.getString(NavArgs.CityName.key))
            val lat = requireNotNull(backStackEntry.arguments?.getString(NavArgs.Lat.key)).toDouble()
            val long = requireNotNull(backStackEntry.arguments?.getString(NavArgs.Long.key)).toDouble()

            ForecastScreen(
                vm = viewModel { ForecastViewModel(cityName, lat, long) },
                onBack = { navController.popBackStack() })
        }
    }
}
