package com.example.collegealart.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R
import com.example.collegealart.data.table.AlertTable
import com.example.collegealart.navigation.ScreensRoute


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventScreen(navController: NavHostController) {

    val alerts = alertViewModel.alerts.observeAsState()
    val showDeleteEventDialog = remember{
        mutableStateOf(false)
    }
    val showUpdateEventDialog = remember{
        mutableStateOf(false)
    }
    val searchedValue = remember {
        mutableStateOf("")
    }
    val deletedEvent = remember {
        mutableStateOf<AlertTable>(AlertTable(
            alertTitle = "",
            date = "",
            time = ""))
    }
    val updatedEvent = remember {
        mutableStateOf<AlertTable>(AlertTable(
            alertTitle = "",
            date = "",
            time = ""))
    }
    val initialValue = remember {
        mutableStateOf("Loading")
    }

    deleteEventDialog(show = showDeleteEventDialog, alert = deletedEvent.value)
    updateEventDialog(show = showUpdateEventDialog, alert = updatedEvent.value, navController = navController)

   Box(){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp, bottom = 90.dp, top = 20.dp)

        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    searchField(
                        modifier = Modifier.weight(4f),
                        value = searchedValue.value,
                        onValueChange = { newValue -> searchedValue.value = newValue },
                        placeholder = "Search"
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.add_event),
                        contentDescription = null,
                        tint = colorResource(id = R.color.appColor1),
                        modifier = Modifier
                            .weight(1f)
                            .padding(5.dp)
                            .clickable {
                                navController.navigate(ScreensRoute.NewEventScreen.route)
                            }
                    )
                }
            }
            if (alerts.value == null) {
                initialValue.value = "Loading.."
            } else if (alerts.value!!.isEmpty()) {
                initialValue.value = "No items"
            } else {
                initialValue.value = ""
                for (alert in alerts.value!!) {
                    item {
                     //   event(alert = alert)
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    deletedEvent.value = alert
                                    showDeleteEventDialog.value = true
                                }else if(it == DismissValue.DismissedToEnd){
                                    updatedEvent.value = alert.copy()
                                    showUpdateEventDialog.value = true
                                }
                                false
                            }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(
                                DismissDirection.StartToEnd,
                                DismissDirection.EndToStart
                            ),
                            background = {

                                val color = when (dismissState.dismissDirection) {
                                    DismissDirection.StartToEnd -> colorResource(id = R.color.appColor1)
                                    DismissDirection.EndToStart -> Color.Red
                                    null -> MaterialTheme.colors.background
                                }
                                when (dismissState.dismissDirection) {
                                    DismissDirection.StartToEnd -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp)
                                                .clip(shape = RoundedCornerShape(20.dp))
                                                .background(color),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .size(50.dp)
                                            )
                                        }
                                    }

                                    else -> {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp)
                                                .clip(shape = RoundedCornerShape(20.dp))
                                                .background(color),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .size(50.dp)
                                            )
                                        }
                                    }
                                }

                            },
                            dismissContent = {
                                event(
                                    alert
                                )
//                                var isVisible by remember { mutableStateOf(false) }
//                                LaunchedEffect(Unit) {
//                                    isVisible = true
//                                }
//                                AnimatedVisibility(
//                                    visible = isVisible,
//                                    enter = fadeIn(animationSpec = tween(durationMillis = 3000)) +
//                                            expandVertically(animationSpec = tween(durationMillis = 2000)),
//                                ) {
//
//                                }

                            }
                        )
                    }
                }
            }
        }
       Box(
           modifier = Modifier.fillMaxSize(),
           contentAlignment = Alignment.Center
       ) {
           Text(
               text = initialValue.value
           )
       }
    }
}

@Composable
fun event(
    alert:AlertTable
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(10.dp)
            .shadow(10.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(20.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp)
                    .height(90.dp)
                  ,
                shape = CircleShape,
                colors = CardDefaults.cardColors(Color.White)
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ){
                    if(alert.imagePath == null) {
                        Image(
                            painter = painterResource(R.drawable.code_alpha),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }else{
                        Image(
                            painter = rememberImagePainter(alert.imagePath),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = alert.alertTitle,
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.appColor1),
                    fontFamily = FontFamily(Font(R.font.bold))
                )

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = alert.date,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.appColor1)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = alert.time,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.appColor1)
                )
            }
        }
    }
}
@Composable
fun searchField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(),
        textStyle = TextStyle(
            fontSize = 16.sp,
            textDecoration = TextDecoration.None,
            color = Color.Black
        ),
        cursorBrush = SolidColor(colorResource(id = R.color.appColor1)),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .height(50.dp)
                    .border(
                        2.dp,
                        color = colorResource(id = R.color.appColor1), RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = colorResource(id = R.color.appColor1)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                     Box(){
                         if (value.isEmpty())
                             Text(
                                 text = placeholder,
                                 color = Color.Gray
                             )
                         innerTextField()
                     }
                    if(value.isNotEmpty()){
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = colorResource(id = R.color.appColor1)
                            )
                        }
                    }
                }
            }
        }
    )
}
@Composable
fun deleteEventDialog(
    show: MutableState<Boolean>,
    alert:AlertTable
    ){

    val selectedAlerts = alertViewModel.selectedIds.observeAsState()

    if(show.value) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.white))
        ) {
            Dialog(
                onDismissRequest = {
                    show.value = false
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)

                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )
                    Text(
                        text = "Are You Sure ?",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.bold)),
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = "You Want To delete this event",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Button(
                            onClick = {
                               alertViewModel.addToSelectedIds(alert.id)
                                selectedAlerts.value?.let { alertViewModel.deleteAlert(it) }
                                show.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Red
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Text(
                                text = "Delete",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                        Button(
                            onClick = {
                                show.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
@Composable
fun updateEventDialog(
    show: MutableState<Boolean>,
    alert:AlertTable,
    navController: NavHostController
){

    if(show.value) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(colorResource(id = R.color.white))
        ) {
            Dialog(
                onDismissRequest = {
                    show.value = false
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)

                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = colorResource(id = R.color.appColor1),
                        modifier = Modifier
                            .size(90.dp)
                            .padding(5.dp)
                    )
                    Text(
                        text = "Are You Sure ?",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.bold)),
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = "You Want To update this event",
                        fontSize = 13.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Button(
                            onClick = {

                               alertViewModel.setSelectedAlertToEdit(alert)
                               navController.navigate(ScreensRoute.UpdateEventScreen.route)
                               show.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = colorResource(id = R.color.appColor1)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1.5f)
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Text(
                                text = "Yes",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                        Button(
                            onClick = {
                                show.value = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(3.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

