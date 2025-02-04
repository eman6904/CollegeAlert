package com.example.collegealart.navigation

sealed class ScreensRoute(val route:String) {
    object EventsScreen : ScreensRoute(route = "events")
    object SplashScreen : ScreensRoute(route = "splashScreen")
    object SettingsScreen : ScreensRoute(route = "settings")
    object ArchiveScreen : ScreensRoute(route = "archive")
    object NewEventScreen : ScreensRoute(route = "newEventScreen")
    object UpdateEventScreen : ScreensRoute(route = "updateEventScreen")

}