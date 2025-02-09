package com.example.collegealart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.collegealart.data.sharedPreference.PreferencesManager
import com.example.collegealart.data.table.AlertTable
import com.example.collegealart.data.viewModel.AlertViewModel
import com.example.collegealart.navigation.BottomBar
import com.example.collegealart.ui.screens.isEventPassed
import com.example.collegealart.ui.theme.CollegeAlartTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var alertViewModel: AlertViewModel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(AlertViewModel::class.java)
        setContent {
            CollegeAlartTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = false
                //to change status bar color
                systemUiController.setStatusBarColor(
                    color = colorResource(id = R.color.appColor1),
                    darkIcons = useDarkIcons
                )
                BottomBar(navController = rememberNavController())
            }
            val alerts = alertViewModel.alerts.observeAsState()
            createNotificationChannel(this)
            if (!alerts.value.isNullOrEmpty())
                scheduleNearestEventNotification(this, alerts.value!!.filter { alert ->
                    !isEventPassed(
                        date = alert.date,
                        time = alert.time
                    )
                }
                )
        }
    }
}

fun parseDateTime(date: String, time: String): Long? {
    return try {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        formatter.parse("$date $time")?.time
    } catch (e: ParseException) {
        null
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "event_channel",
            "Event Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for event notifications"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}

fun scheduleNearestEventNotification(context: Context, alertList: List<AlertTable>) {
    val currentTime = System.currentTimeMillis()

    val nearestEvent = alertList.mapNotNull { alert ->
        parseDateTime(alert.date, alert.time)?.let { eventTime ->
            if (eventTime > currentTime) Pair(eventTime, alert.alertTitle) else null
        }
    }.minByOrNull { it.first }

    nearestEvent?.let { (eventTime, eventTitle) ->
        val delay = eventTime - currentTime

        val workRequest = OneTimeWorkRequestBuilder<EventNotificationWorker>()
            .setInputData(
                Data.Builder()
                    .putString("event_title", eventTitle)
                    .putLong("event_time", eventTime)
                    .build()
            )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}

class EventNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val eventTime = inputData.getLong("event_time", -1)
        val eventTitle = inputData.getString("event_title") ?: return Result.failure()

        if (eventTime == -1L) return Result.failure()

        val sherdPref = PreferencesManager(applicationContext)
        val currentTime = System.currentTimeMillis()

        if (isSameMinute(eventTime, currentTime) && sherdPref.getValue("Notifications")) {
            sendNotification(eventTime, eventTitle)
        }

        return Result.success()
    }

    private fun isSameMinute(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = time2 }

        return calendar1[Calendar.YEAR] == calendar2[Calendar.YEAR] &&
                calendar1[Calendar.MONTH] == calendar2[Calendar.MONTH] &&
                calendar1[Calendar.DAY_OF_MONTH] == calendar2[Calendar.DAY_OF_MONTH] &&
                calendar1[Calendar.HOUR_OF_DAY] == calendar2[Calendar.HOUR_OF_DAY] &&
                calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE]
    }

    private fun sendNotification(eventTime: Long, title: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmapOptions = BitmapFactory.Options().apply { inScaled = false } // تحسين الدقة
        val largeIcon = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.reminder, bitmapOptions)

        val notification = NotificationCompat.Builder(applicationContext, "event_channel")
            .setContentTitle("Welcome Dear ^^")
            .setContentText("We want to remind you of ${title} at ${DateFormat.format("hh:mm a", eventTime)}!")
            .setSmallIcon(R.drawable.college_alert_app_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeIcon))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(eventTime.hashCode(), notification)
    }
}
