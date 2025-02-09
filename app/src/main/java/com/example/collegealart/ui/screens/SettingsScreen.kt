package com.example.collegealart.ui.screens

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R

@Composable
fun SettingsScreen(navController: NavHostController){

    val sharedPreferences = alertViewModel.sharedPrefManager.observeAsState()
    val context = LocalContext.current
    val a = remember{
        mutableStateOf(sharedPreferences.value!!.getValue(context.getString(R.string.notifications)))
    }
    val b = remember{
        mutableStateOf(sharedPreferences.value!!.getValue(context.getString(R.string.expiredevents)))
    }

    LaunchedEffect(a.value){
        sharedPreferences.value!!.saveValue(context.getString(R.string.notifications),a.value)
    }
    LaunchedEffect(b.value){
        sharedPreferences.value!!.saveValue(context.getString(R.string.expiredevents),b.value)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(R.string.settings),
            fontFamily = FontFamily(Font(R.font.bold)),
            fontSize = 25.sp,
            color = colorResource(id = R.color.appColor1),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(20.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .padding(5.dp)
                    .background(color = colorResource(id = R.color.appColor1)),
            )
            Text(
                text = stringResource(R.string.enable_notification)
            )
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ){
                Switch(
                    checked = a.value,
                    onCheckedChange = {it -> a.value = it},
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor =   colorResource(id = R.color.appColor1),
                        checkedTrackColor = colorResource(id = R.color.appColor1),
                        uncheckedTrackColor = Color.White,
                        uncheckedThumbColor = colorResource(id = R.color.appColor1)
                    )
                )
            }
        }
       Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .padding(5.dp)
                    .background(color = colorResource(id = R.color.appColor1)),
            )
            Text(
                text = stringResource(R.string.auto_remove_expired_events)
            )
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ){
                Switch(
                    checked = b.value,
                    onCheckedChange = {it -> b.value = it },
                    colors = SwitchDefaults.colors(
                        uncheckedBorderColor =   colorResource(id = R.color.appColor1),
                        checkedTrackColor = colorResource(id = R.color.appColor1),
                        uncheckedTrackColor = Color.White,
                        uncheckedThumbColor = colorResource(id = R.color.appColor1)
                    )
                )
            }
        }
    }
}