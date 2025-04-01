package com.rndeveloper.myapplication.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rndeveloper.myapplication.App
import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.data.datasource.CitiesInfoLocalDataSource
import com.rndeveloper.myapplication.data.datasource.LocationDataSource
import com.rndeveloper.myapplication.data.datasource.RegionDataSource
import com.rndeveloper.myapplication.data.datasource.WeatherRemoteDataSource
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastScreen
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastViewModel
import com.rndeveloper.myapplication.ui.screens.home.HomeScreen
import com.rndeveloper.myapplication.ui.screens.home.HomeViewModel

@Composable
fun Navigation() {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as App

    val weatherRepository =
        WeatherRepository(WeatherRemoteDataSource(), CitiesInfoLocalDataSource(app.db.citiesDao()))
    val regionRepository = RegionRepository(RegionDataSource(app, LocationDataSource(app)))

    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {

            HomeScreen(
                vm = viewModel { HomeViewModel(weatherRepository, regionRepository) },
                onForecastClick = { cityName: String, lat: String, long: String ->
                    navController.navigate(NavScreen.Forecast.createRoute(cityName, lat, long))
                }
            )
        }
        composable(
            route = NavScreen.Forecast.route,
            arguments = listOf(
                navArgument(NavArgs.CityName.key) { type = NavType.StringType },
                navArgument(NavArgs.Lat.key) {
                    type = NavType.StringType
                },  // Double no es soportado, usa String
                navArgument(NavArgs.Long.key) {
                    type = NavType.StringType
                }  // Double no es soportado, usa String
            )
        ) { backStackEntry ->
            val cityName = requireNotNull(backStackEntry.arguments?.getString(NavArgs.CityName.key))
            val lat =
                requireNotNull(backStackEntry.arguments?.getString(NavArgs.Lat.key)).toDouble()
            val long =
                requireNotNull(backStackEntry.arguments?.getString(NavArgs.Long.key)).toDouble()

            ForecastScreen(
                vm = viewModel {
                    ForecastViewModel(cityName, lat, long, weatherRepository)
                },
                onBack = { navController.popBackStack() })
        }
    }
}
