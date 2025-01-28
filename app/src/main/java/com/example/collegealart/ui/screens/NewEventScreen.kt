package com.example.collegealart.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.collegealart.R
import java.util.Calendar

@Composable
fun NewEventScreen(navController: NavHostController) {

    val title = remember {
        mutableStateOf("")
    }
    val aboutEvent = remember {
        mutableStateOf("")
    }
    val location = remember {
        mutableStateOf("")
    }
    val selectedDate = remember {
        mutableStateOf("")
    }
    val selectedTime = remember {
        mutableStateOf("")
    }

   Box(
       modifier = Modifier.fillMaxSize()
   ){
       Column(
           modifier = Modifier
               .padding(bottom = 0.dp)
               .fillMaxSize()
       ){
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(1f),
               contentAlignment = Alignment.CenterStart
           ){
               Box(
                   modifier = Modifier
                       .size(200.dp)
                       .clip(shape = CircleShape)
                       .background(colorResource(id = R.color.circleColor))
               )
           }
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .weight(1f),
               contentAlignment = Alignment.Center
           ){
               Box(
                   modifier = Modifier
                       .size(160.dp)
                       .clip(shape = CircleShape)
                       .background(colorResource(id = R.color.circleColor))
               )
           }
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(end = 35.dp)
                   .weight(1f),
               contentAlignment = Alignment.TopEnd
           ){
               Box(
                   modifier = Modifier
                       .size(100.dp)
                       .clip(shape = CircleShape)
                       .background(colorResource(id = R.color.circleColor))
               )
           }
       }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "New Event",
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
                placeholder = "Title"
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            underlinedTextField(
                value = aboutEvent.value,
                onValueChange = { newValue -> aboutEvent.value = newValue },
                placeholder = "About Event"
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            underlinedTextField(
                value = location.value,
                onValueChange = { newValue -> location.value = newValue },
                placeholder = "Location"
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            calenderView(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                placeholder = "Select date"
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            calenderView(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                placeholder = "Select time"
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                imagePicker()
                soundPicker()
            }
        }

    }
  }
}

@Composable
fun calenderView(
    selectedDate: MutableState<String>,
    selectedTime: MutableState<String>,
    placeholder: String
) {

    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mAM_BM = mCalendar[Calendar.AM_PM]
    val mTimePickerDialog = android.app.TimePickerDialog(
        mContext,
        R.style.DatePickerTheme,
        { _, mHour: Int, mMinute: Int ->
           selectedTime.value = "$mHour:$mMinute $mAM_BM"
        }, mHour, mMinute, false
    )
    val mYear = mCalendar[Calendar.YEAR]
    val mMonth = mCalendar[Calendar.MONTH]
    val mDay = mCalendar[Calendar.DAY_OF_MONTH]
    val mDatePickerDialog = DatePickerDialog(
        ContextThemeWrapper(mContext, R.style.DatePickerTheme), // الثيم المخصص
        { _, year, month, dayOfMonth ->
            selectedDate.value = "$dayOfMonth/${month + 1}/$year"
        },
        mYear,
        mMonth,
        mDay
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                if (placeholder == "Select date")
                    mDatePickerDialog.show()
                else
                    mTimePickerDialog.show()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter =if(placeholder=="Select date") painterResource(id = R.drawable.calendar)
                else
                    painterResource(id = R.drawable.clock_icon) ,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colorResource(id = R.color.appColor1)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if(placeholder=="Select date"){

                    if (selectedDate.value.isEmpty())
                        placeholder else selectedDate.value
                }else{

                    if (selectedTime.value.isEmpty())
                        placeholder else selectedTime.value
                },
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.appColor1))
                .shadow(20.dp)
        )
    }
}

@Composable
fun underlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            textDecoration = TextDecoration.None,
            color = Color.Black
        ),
        cursorBrush = SolidColor(colorResource(id = R.color.appColor1)),
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if(placeholder == "Location"){
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(id = R.color.appColor1)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (value.isEmpty())
                        Text(
                            text = placeholder,
                            color = Color.Gray
                        )
                    innerTextField()
                   if(placeholder!="Location"){
                       Spacer(modifier = Modifier.width(8.dp))
                       Box(
                           modifier = Modifier
                               .weight(1f)
                               .fillMaxWidth(),
                           contentAlignment = Alignment.CenterEnd
                       ) {
                           Icon(
                               imageVector = Icons.Default.Edit,
                               contentDescription = null,
                               modifier = Modifier.size(24.dp),
                               tint = colorResource(id = R.color.appColor1)
                           )
                       }
                   }
                }
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(color = colorResource(id = R.color.appColor1))
                        .shadow(20.dp)
                )
            }
        }
    )
}

@Composable
fun imagePicker() {
    // Create the launcher
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
        }
    )
    Box(
        modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, colorResource(id = R.color.appColor1), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(imageUri!=null){
                Image(
                    painter = rememberImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }else{
                Icon(
                    painter = painterResource(id = R.drawable.image_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(5.dp)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    tint = colorResource(id = R.color.appColor1)
                )
            }
            Text(
                text = "upload image",
                fontSize = 10.sp,
                color = colorResource(id = R.color.appColor1)
            )
        }
    }
}
@Composable
fun soundPicker() {
    // Create the launcher
    var soundUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val soundPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            soundUri = uri
        }
    )
    Box(
        modifier = Modifier
            .height(150.dp)
            .width(150.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, colorResource(id = R.color.appColor1), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                painter = painterResource(id = R.drawable.upload_sound_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(5.dp)
                    .clickable {
                        soundPickerLauncher.launch("audio/*")
                    },
                tint = colorResource(id = R.color.appColor1)
            )
            Text(
                text = "upload sound",
                fontSize = 10.sp,
                color = colorResource(id = R.color.appColor1)
            )
        }
    }
    LaunchedEffect(soundUri){
        if(soundUri!=null)
            playSound(context, soundUri!!)
    }

}
fun playSound(context: Context, soundUri: Uri) {
    val mediaPlayer = MediaPlayer()
    mediaPlayer.setDataSource(context, soundUri)
    mediaPlayer.prepare()
    mediaPlayer.start()
}



