package com.example.collegealart.ui.screens

import android.graphics.Paint.Style
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.collegealart.R


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventScreen(navController: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp, bottom = 90.dp, top = 20.dp)

    ) {
        for (i in 0..6) {
            item {
                val dismissState = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {

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
                            DismissDirection.StartToEnd -> Color.Green
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
                        event()
                    }
                )
            }
        }
    }
}

@Composable
fun event() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(10.dp)
            .clip(shape = RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.appColor1)),
        elevation = CardDefaults.cardElevation(20.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically

        ) {

            Card(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(40.dp))
                    .fillMaxSize()
                    .weight(1f)
                    .padding(10.dp)
                    .height(80.dp)
                    .width(100.dp)
            ){
                Image(
                    painter = painterResource(R.drawable.events_image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "CodeAlpha First Task",
                    fontSize = 15.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.bold))
                )

                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "1/26/2025",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}