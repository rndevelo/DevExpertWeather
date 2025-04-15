package com.rndeveloper.myapplication.ui.navigation

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.location.LocationServices
import com.rndeveloper.myapplication.App
import com.rndeveloper.myapplication.data.RegionRepository
import com.rndeveloper.myapplication.data.WeatherRepository
import com.rndeveloper.myapplication.framework.GeocoderRegionDataSource
import com.rndeveloper.myapplication.framework.PlayServicesLocationDataSource
import com.rndeveloper.myapplication.framework.WeatherRoomDataSource
import com.rndeveloper.myapplication.framework.WeatherServerDataSource
import com.rndeveloper.myapplication.framework.remote.WeatherClient
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastScreen
import com.rndeveloper.myapplication.ui.screens.forecast.ForecastViewModel
import com.rndeveloper.myapplication.ui.screens.home.HomeScreen
import com.rndeveloper.myapplication.ui.screens.home.HomeViewModel
import com.rndeveloper.myapplication.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.usecases.GetLocationCityUseCase
import com.rndeveloper.myapplication.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.usecases.ToggleCityUseCase

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Navigation() {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as App

    val weatherRepository =
        WeatherRepository(
            WeatherRoomDataSource(app.db.weatherDao()), WeatherServerDataSource(
                WeatherClient.instance
            )
        )
    val regionRepository = RegionRepository(
        GeocoderRegionDataSource(
            Geocoder(app),
            PlayServicesLocationDataSource(LocationServices.getFusedLocationProviderClient(app))
        )
    )

    NavHost(
        navController = navController,
        startDestination = NavScreen.Home.route
    ) {
        composable(NavScreen.Home.route) {

            HomeScreen(
                vm = viewModel {
                    HomeViewModel(
                        GetWeatherUseCase(weatherRepository),
                        GetFavCitiesUseCase(weatherRepository),
                        SearchCitiesUseCase(weatherRepository),
                        ToggleCityUseCase(weatherRepository),
                        GetLocationCityUseCase(regionRepository)
                    )
                },
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
                    ForecastViewModel(cityName, lat, long, GetWeatherUseCase(weatherRepository))
                },
                onBack = { navController.popBackStack() })
        }
    }
}
