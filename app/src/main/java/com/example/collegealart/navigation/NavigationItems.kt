package com.example.collegealart.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItems(
    val route:String,
    val title:String,
    val icon: ImageVector
) {
    object Events : NavigationItems(
        route = "events",
        title = "Events",
        icon = Icons.Default.Home
    )
    object Settings : NavigationItems(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
    object Archive : NavigationItems(
        route = "archive",
        title = "Archive",
        icon = Icons.Default.Home
    )
    

}