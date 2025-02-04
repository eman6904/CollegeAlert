package com.example.collegealart.navigation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.collegealart.R

@Composable
fun BottomBar(navController: NavHostController) {

    var showBottomBar by rememberSaveable { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    showBottomBar = when (navBackStackEntry?.destination?.route) {
        NavigationItems.Events.route -> true
        NavigationItems.Settings.route -> true
        NavigationItems.Archive.route -> true
        else -> false
    }
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                val screens = listOf(
                    NavigationItems.Events,
                    NavigationItems.Settings,
                    NavigationItems.Archive,
                )
                val currentDestination = navBackStackEntry?.destination
                NavigationBar(
                    containerColor = colorResource(id = R.color.appColor1),
                    modifier= Modifier
                        .clip(RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp))
                        .shadow(
                            40.dp,
                            spotColor = Color.Black,
                            shape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp)
                        ),

                ) {
                    screens.forEach() {
                        when(it){
                            NavigationItems.Events->{addItem(it, currentDestination, navController,R.drawable.schedule,it.title)}
                            NavigationItems.Archive->{addItem(it, currentDestination, navController,R.drawable.archive,it.title)}
                            else->{addItem(it, currentDestination, navController,R.drawable.user_settings,it.title)}
                        }
                    }
                }
            }
        }
    ) { AppNavGrav(navController = navController) }
}

@Composable
fun RowScope.addItem(
    screen: NavigationItems,
    currentDestination: NavDestination?,
    navController: NavHostController,
    icon:Int,
    title:String
) {
    NavigationBarItem(
        modifier = Modifier,
        icon = {
            Icon(
                painter = painterResource(icon),
                modifier = Modifier.size(30.dp),
                contentDescription = "navigation icon",

            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {navController.navigate(screen.route) },
        label = { Text(
            text = title
        ) },
            colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.White,
                selectedTextColor = Color.White,
                unselectedTextColor = colorResource(id = R.color.white),
                selectedIconColor = colorResource(id = R.color.appColor1),
                unselectedIconColor = colorResource(id = R.color.white)
        )
    )
}