package com.example.collegealart.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R
import com.example.collegealart.data.table.AlertTable

@Composable
fun UpdateEventScreen(navController:NavHostController){

    val selectedAlert = alertViewModel.selectedAlertToEdit.observeAsState()
    val title = remember {
        mutableStateOf(selectedAlert.value!!.alertTitle)
    }
    val aboutEvent = remember {
        mutableStateOf<String?>(selectedAlert.value!!.aboutAlert)
    }
    val location = remember {
        mutableStateOf<String?>(selectedAlert.value!!.location)
    }
    val selectedDate = remember {
        mutableStateOf(selectedAlert.value!!.date)
    }
    val selectedTime = remember {
        mutableStateOf(selectedAlert.value!!.time)
    }
    var imagePath = remember {
        mutableStateOf<String?>(selectedAlert.value!!.imagePath)
    }
    var soundPath = remember {
        mutableStateOf<String?>(selectedAlert.value!!.soundPath)
    }
    val emptyTitle = remember {
        mutableStateOf(false)
    }
    val emptyDate = remember {
        mutableStateOf(false)
    }
    val emptyTime = remember {
        mutableStateOf(false)
    }
    val showSaveAlertDialog = remember {
        mutableStateOf(false)
    }
    saveAlertDialog(show = showSaveAlertDialog, navController = navController)
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Edit Event",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier.padding(30.dp),
                    color = colorResource(id = R.color.appColor1),
                    textDecoration = TextDecoration.Underline
                )
            }
            item {
                underlinedTextField(
                    value = title.value,
                    onValueChange = { newValue -> title.value = newValue },
                    emptyTitle = emptyTitle,
                    placeholder = "Title"
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                underlinedTextField(
                    value = aboutEvent.value?:"",
                    onValueChange = { newValue -> aboutEvent.value = newValue },
                    placeholder = "About Event"
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                underlinedTextField(
                    value = location.value?:"",
                    onValueChange = { newValue -> location.value = newValue },
                    placeholder = "Location"
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                calenderView(
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    placeholder = "Select date",
                    isEmpty = emptyDate
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                calenderView(
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    placeholder = "Select time",
                    isEmpty = emptyTime
                )
            }
            item {
                Spacer(modifier = Modifier.height(15.dp))
                Row {

                    imagePicker(
                        imagePath = imagePath
                    )
                    soundPicker(
                        soundPath = soundPath
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = {
                        emptyDate.value = selectedDate.value.isEmpty()
                        emptyTime.value = selectedTime.value.isEmpty()
                        emptyTitle.value = title.value.isEmpty()
                        if(!emptyTime.value&&!emptyDate.value&&!emptyTitle.value){
                            Log.d("Alert",AlertTable(
                                id = selectedAlert.value!!.id,
                                alertTitle = title.value,
                                aboutAlert = aboutEvent.value,
                                location = location.value,
                                date = selectedDate.value,
                                time = selectedTime.value,
                                imagePath = imagePath.value,
                                soundPath = soundPath.value
                            ).toString())
                            alertViewModel.updateAlert(
                                AlertTable(
                                    id = selectedAlert.value!!.id,
                                    alertTitle = title.value,
                                    aboutAlert = aboutEvent.value,
                                    location = location.value,
                                    date = selectedDate.value,
                                    time = selectedTime.value,
                                    imagePath = imagePath.value,
                                    soundPath = soundPath.value
                                )
                            )
                            showSaveAlertDialog.value = true
                        }

                    },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .shadow(10.dp, RoundedCornerShape(20.dp))
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = colorResource(id = R.color.appColor1)
                    )
                ) {
                    Text(
                        text = "Done",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.bold)),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }

        }
    }

}