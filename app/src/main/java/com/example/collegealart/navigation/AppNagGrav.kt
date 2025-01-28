package com.example.collegealart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.collegealart.ui.screens.ArchiveScreen
import com.example.collegealart.ui.screens.EventScreen
import com.example.collegealart.ui.screens.NewEventScreen
import com.example.collegealart.ui.screens.SettingsScreen
import com.example.collegealart.ui.screens.SplashScreen


@Composable
fun AppNavGrav(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination =ScreensRoute.SplashScreen.route

    ) {
        composable(ScreensRoute.EventsScreen.route){
            EventScreen(navController)
        }
        composable(ScreensRoute.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(ScreensRoute.SettingsScreen.route){
            SettingsScreen(navController)
        }
        composable(ScreensRoute.ArchiveScreen.route){
            ArchiveScreen(navController)
        }
        composable(ScreensRoute.NewEventScreen.route){
            NewEventScreen(navController)
        }

    }
}