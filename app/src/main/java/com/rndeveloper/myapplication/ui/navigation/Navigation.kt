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
import androidx.navigation.toRoute
import com.google.android.gms.location.LocationServices
import com.rndeveloper.myapplication.App
import com.rndeveloper.myapplication.home.HomeScreen
import com.rndeveloper.myapplication.home.HomeViewModel
import com.rndeveloper.myapplication.location.GeocoderRegionDataSource
import com.rndeveloper.myapplication.location.GetLocationCityUseCase
import com.rndeveloper.myapplication.location.PlayServicesLocationDataSource
import com.rndeveloper.myapplication.location.RegionRepositoryImpl
import com.rndeveloper.myapplication.forecast.ForecastScreen
import com.rndeveloper.myapplication.forecast.ForecastViewModel
import com.rndeveloper.myapplication.weather.WeatherRepositoryImpl
import com.rndeveloper.myapplication.weather.local.WeatherRoomDataSource
import com.rndeveloper.myapplication.weather.remote.WeatherClient
import com.rndeveloper.myapplication.weather.remote.WeatherServerDataSource
import com.rndeveloper.myapplication.weather.usecases.GetFavCitiesUseCase
import com.rndeveloper.myapplication.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.weather.usecases.SearchCitiesUseCase
import com.rndeveloper.myapplication.weather.usecases.ToggleCityUseCase

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Navigation() {

    val navController = rememberNavController()

    val app = LocalContext.current.applicationContext as App

    val weatherRepository =
        WeatherRepositoryImpl(
            WeatherRoomDataSource(app.db.weatherDao()), WeatherServerDataSource(
                WeatherClient.instance
            )
        )
    val regionRepository = RegionRepositoryImpl(
        GeocoderRegionDataSource(
            Geocoder(app),
            PlayServicesLocationDataSource(LocationServices.getFusedLocationProviderClient(app))
        )
    )

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {

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
                    navController.navigate(Forecast(cityName, lat, long))
                }
            )
        }
        composable<Forecast> { backStackEntry ->
            val forecast = backStackEntry.toRoute<Forecast>()
            ForecastScreen(
                vm = viewModel {
                    ForecastViewModel(
                        forecast.cityName,
                        forecast.lat,
                        forecast.long,
                        GetWeatherUseCase(weatherRepository)
                    )
                },
                onBack = { navController.popBackStack() })
        }
    }
}
