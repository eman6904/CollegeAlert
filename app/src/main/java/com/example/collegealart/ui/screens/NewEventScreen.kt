package com.example.collegealart.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.text.format.DateFormat
import android.view.ContextThemeWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.collegealart.MainActivity.Companion.alertViewModel
import com.example.collegealart.R
import com.example.collegealart.data.table.AlertTable
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

@Composable
fun NewEventScreen(navController: NavHostController) {

    val title = remember {
        mutableStateOf("")
    }
    val aboutEvent = remember {
        mutableStateOf<String?>(null)
    }
    val location = remember {
        mutableStateOf<String?>(null)
    }
    val selectedDate = remember {
        mutableStateOf("")
    }
    val selectedTime = remember {
        mutableStateOf("")
    }
    var imagePath = remember {
        mutableStateOf<String?>(null)
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
                text = stringResource(R.string.new_event),
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
                placeholder = stringResource(R.string.title)
            )
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            underlinedTextField(
                value = aboutEvent.value?:"",
                onValueChange = { newValue -> aboutEvent.value = newValue },
                placeholder = stringResource(R.string.about_event)
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
            underlinedTextField(
                value = location.value?:"",
                onValueChange = { newValue -> location.value = newValue },
                placeholder = stringResource(R.string.location)
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
            calenderView(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                placeholder = stringResource(R.string.select_date),
                isEmpty = emptyDate
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
            calenderView(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                placeholder = stringResource(R.string.select_time),
                isEmpty = emptyTime
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
            Row {
                imagePicker(
                    imagePath = imagePath
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
                        alertViewModel.insertAlert(
                            AlertTable(
                                alertTitle = title.value,
                                aboutAlert = aboutEvent.value,
                                location = location.value,
                                date = selectedDate.value,
                                time = selectedTime.value,
                                imagePath = imagePath.value,
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
                    text = stringResource(R.string.done),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

    }
  }
}

@Composable
fun calenderView(
    selectedDate: MutableState<String>,
    selectedTime: MutableState<String>,
    placeholder: String,
    isEmpty :MutableState<Boolean>
) {

    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTimePickerDialog = android.app.TimePickerDialog(
        mContext,
        R.style.DatePickerTheme,
        { _, mHour: Int, mMinute: Int ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, mHour)
                set(Calendar.MINUTE, mMinute)
            }
            selectedTime.value = DateFormat.format("hh:mm a", calendar).toString()
        }, mHour, mMinute, false
    )
    val mYear = mCalendar[Calendar.YEAR]
    val mMonth = mCalendar[Calendar.MONTH]
    val mDay = mCalendar[Calendar.DAY_OF_MONTH]
    val mDatePickerDialog = DatePickerDialog(
        ContextThemeWrapper(mContext, R.style.DatePickerTheme),
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
                if (placeholder == mContext.getString(R.string.select_date))
                    mDatePickerDialog.show()
                else
                    mTimePickerDialog.show()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter =if(placeholder == mContext.getString(R.string.select_date)) painterResource(id = R.drawable.calendar)
                else
                    painterResource(id = R.drawable.clock_icon) ,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = colorResource(id = R.color.appColor1)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if(placeholder==mContext.getString(R.string.select_date)){

                    if (selectedDate.value.isEmpty())
                        placeholder else selectedDate.value
                }else{

                    if (selectedTime.value.isEmpty())
                        placeholder else selectedTime.value
                },
                color = if(placeholder==mContext.getString(R.string.select_date)){

                    if (selectedDate.value.isEmpty())
                        Color.Gray else Color.Black
                }else{

                    if (selectedTime.value.isEmpty())
                        Color.Gray else Color.Black
                },
                modifier = Modifier.weight(1f)
            )
            Box(
                contentAlignment = Alignment.CenterEnd,
            ) {
               Text(
                   text =" * ",
                   color = Color.Red,
               )
            }
        }
        if(isEmpty.value){
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = Color.Red)
                    .shadow(20.dp)
            )
        }else{
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth()
                    .background(color = colorResource(id = R.color.appColor1))
                    .shadow(20.dp)
            )
        }
    }
}

@Composable
fun underlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    emptyTitle:MutableState<Boolean> = mutableStateOf(false)
) {
    val context = LocalContext.current
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
                    if(placeholder == context.getString(R.string.location)){
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = colorResource(id = R.color.appColor1)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }else if(placeholder == context.getString(R.string.title)){
                        Box(
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(
                                text ="*  ",
                                color = Color.Red,
                            )
                        }
                    }
                  Box(
                      modifier = Modifier
                      .weight(1f)
                  ){
                      if (value.isEmpty())
                          Text(
                              text = placeholder,
                              color = Color.Gray
                          )
                      innerTextField()
                  }
                   if(placeholder!=context.getString(R.string.location)){
                       Spacer(modifier = Modifier.width(8.dp))
                       Box(
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
                if(emptyTitle.value){
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(Color.Red)
                            .shadow(20.dp)
                    )
                }else{
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(color = colorResource(id = R.color.appColor1))
                            .shadow(20.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun imagePicker(
    imagePath:MutableState<String?>
) {
    val imageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val delete = remember{ mutableStateOf(false) }
    val context = LocalContext.current

    if(delete.value){
        deleteUri(
            show = delete ,
            uri = imageUri,
            path = imagePath
        )
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri.value = uri
        }
    )
    LaunchedEffect(imageUri.value){

        if(imageUri.value!=null)
           imagePath.value = saveImageToInternalStorage(context = context, uri = imageUri.value!!)

    }
    Box(
        modifier = Modifier
            .height(155.dp)
            .width(300.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, colorResource(id = R.color.appColor1), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.TopEnd
        ){
            if(imagePath.value!=null){
                Icon(
                    Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            delete.value = true
                        },
                    tint = colorResource(id = R.color.appColor1)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(imagePath.value!=null){

                Image(
                    painter = rememberImagePainter(imagePath.value),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(R.string.image_uploaded_successfully),
                    fontSize = 13.sp,
                    color = Color.Green
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
                Text(
                    text = stringResource(R.string.upload_image),
                    fontSize = 10.sp,
                    color = colorResource(id = R.color.appColor1)
                )
            }
        }
    }
}
fun deleteFile(imagePath:String) {
    val file = File(imagePath)
    if(file.exists())
        file.delete()
}
fun saveImageToInternalStorage(context: Context, uri: Uri): String? {

    val inputStream = context.contentResolver.openInputStream(uri) ?: return null
    val file = File(context.filesDir, "${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)

    inputStream.copyTo(outputStream)
    inputStream.close()
    outputStream.close()

    return file.absolutePath
}

@Composable
fun deleteUri(
    show:MutableState<Boolean>,
    uri:MutableState<Uri?>,
    path:MutableState<String?>,

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
                    Spacer(modifier = Modifier.height(25.dp))
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(5.dp)
                    )
                    Text(
                        text = stringResource(R.string.are_you_sure),
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.bold)),
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = stringResource(R.string.you_want_to_delete_this_file),
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
                                deleteFile(path.value!!)
                                path.value = null
                                uri.value = null
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
                                text = stringResource(R.string.delete),
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
                                text = stringResource(R.string.cancel),
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
fun saveAlertDialog(
    show:MutableState<Boolean>,
    navController: NavHostController
){
    if(show.value){
        Card(
           shape = RoundedCornerShape(20.dp),
        ) {
            Dialog(
                onDismissRequest = {
                    show.value = false
                }
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))


                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.done_animation))
                    val progress by animateLottieCompositionAsState(
                        composition = composition,
                        iterations = 1
                    )
                    if (composition != null) {
                        LottieAnimation(
                            composition = composition,
                            progress = progress,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(10.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                    if(progress==1f){
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                            show.value = false
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

            }
        }
    }
}





