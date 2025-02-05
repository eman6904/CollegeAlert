package com.example.collegealart.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R
import com.example.collegealart.data.table.AlertTable
import com.example.collegealart.navigation.ScreensRoute
import com.example.collegealart.parseDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArchiveScreen(navController: NavHostController) {

    val alerts = alertViewModel.alerts.observeAsState()

    val events = remember {
        mutableStateOf<List<AlertTable>>(alerts.value!!)
    }
    val showDeleteEventDialog = remember {
        mutableStateOf(false)
    }
    val showUpdateEventDialog = remember {
        mutableStateOf(false)
    }
    val searchedValue = remember {
        mutableStateOf("")
    }
    val deletedEvent = remember {
        mutableStateOf<AlertTable>(
            AlertTable(
                alertTitle = "",
                date = "",
                time = ""
            )
        )
    }
    val updatedEvent = remember {
        mutableStateOf<AlertTable>(
            AlertTable(
                alertTitle = "",
                date = "",
                time = ""
            )
        )
    }
    val initialValue = remember {
        mutableStateOf("Loading")
    }
    LaunchedEffect(searchedValue.value) {
        events.value = alerts.value!!.filter { alert ->
            alert.alertTitle.lowercase()
                .contains(searchedValue.value.lowercase(), true)
                    && isEventPassed(
                date = alert.date,
                time = alert.time
            )
        }
        if (events.value == null) {
            initialValue.value = "Loading.."
        } else if (events.value!!.isEmpty()) {
            initialValue.value = "No items"
        } else {
            initialValue.value = ""
        }
    }
    deleteEventDialog(show = showDeleteEventDialog, alert = deletedEvent.value)
    updateEventDialog(
        show = showUpdateEventDialog,
        alert = updatedEvent.value,
        navController = navController
    )

    Box() {
        Column(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            searchField(
                modifier = Modifier.padding(10.dp),
                value = searchedValue.value,
                onValueChange = { newValue -> searchedValue.value = newValue },
                placeholder = "Search"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, end = 5.dp, bottom = 80.dp)

            ) {
                items(
                    items = events.value,
                    key = { item: AlertTable -> item.id }
                ) { alert ->

                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            if (it == DismissValue.DismissedToStart) {
                                deletedEvent.value = alert
                                showDeleteEventDialog.value = true
                            } else if (it == DismissValue.DismissedToEnd) {
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

                            var isVisible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                isVisible = true
                            }
                            AnimatedVisibility(
                                visible = isVisible,
                                enter = fadeIn(animationSpec = tween(durationMillis = 3000)) +
                                        expandVertically(animationSpec = tween(durationMillis = 2000)),
                            ) {
                                invalidEvent(
                                    alert
                                )

                            }

                        }
                    )
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
fun invalidEvent(
    alert: AlertTable
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(10.dp)
            .shadow(10.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(colorResource(id = R.color.invalidEventColor)),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp)
                    .height(90.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(Color.White)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (alert.imagePath == null) {
                        Image(
                            painter = painterResource(R.drawable.college_alert_app_icon),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
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

@RequiresApi(Build.VERSION_CODES.O)
fun isEventPassed(date: String, time: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy hh:mm a")
    val eventTime = LocalDateTime.parse("$date $time", formatter)
    val currentTime = LocalDateTime.now()
    return eventTime.isBefore(currentTime)
}