package com.example.collegealart.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.collegealart.R
import com.example.collegealart.navigation.ScreensRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun SplashScreen(navController: NavHostController){

   Box(
       modifier = Modifier.fillMaxSize(),
       contentAlignment = Alignment.Center
   ){
       val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation)) // Replace with your file name

       val progress by animateLottieCompositionAsState(
           composition = composition,
           iterations = 1
       )


       if (composition != null) {
           LottieAnimation(
               composition = composition,
               progress = progress,
               modifier = Modifier
                   .fillMaxWidth()
                   .height(200.dp)
           )
       }
    if(progress==1f){
        LaunchedEffect(Unit) {
            navController.navigate(ScreensRoute.EventsScreen.route) {
                popUpTo(ScreensRoute.SplashScreen.route) { inclusive = true }
            }
        }
    }
   }
}