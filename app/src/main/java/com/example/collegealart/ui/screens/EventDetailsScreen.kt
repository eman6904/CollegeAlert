package com.example.collegealart.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R

@Composable
fun EventDetailsScreen(navController: NavHostController) {

    val event = alertViewModel.selectedAlertToDisplay.observeAsState()
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp,top = 30.dp)
    ) {
        item {
            Text(
                text = event.value!!.alertTitle,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.bold)),
                modifier = Modifier.padding(10.dp)
            )
        }
        item {
            Text(
                text = event.value!!.aboutAlert ?: "",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }
        item {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .height(155.dp)
                    .width(300.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                if (event.value!!.imagePath == null) {
                    Image(
                        painter = painterResource(R.drawable.college_alert_app_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = rememberImagePainter(event.value!!.imagePath),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Icon(
                    painterResource(id = R.drawable.calendar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 5.dp)
                        .padding(end = 5.dp),
                    tint = colorResource(id = R.color.appColor1)
                )
                Text(
                    text = event.value!!.date,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

            }
        }
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Icon(
                    painterResource(id = R.drawable.clock_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp),
                    tint = colorResource(id = R.color.appColor1)
                )
                Text(
                    text = event.value!!.time,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

            }
        }
        item{
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 5.dp),
                    tint = colorResource(id = R.color.appColor1)
                )
                Text(
                    text = event.value!!.location ?: "not available",
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )

            }
        }
    }
}