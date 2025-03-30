package com.rndeveloper.myapplication.ui.navigation

sealed class NavScreen(val route: String) {
    data object Home : NavScreen("home")

    data object Forecast : NavScreen("detail/{${NavArgs.CityName.key}}/{${NavArgs.Lat.key}}/{${NavArgs.Long.key}}") {
        fun createRoute(cityName: String, lat: String, long: String) = "detail/$cityName/$lat/$long"
    }
}

enum class NavArgs(val key: String) {
    CityName("cityName"),
    Lat("lat"),
    Long("long")
}