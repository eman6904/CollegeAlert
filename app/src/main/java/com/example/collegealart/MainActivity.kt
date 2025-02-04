package com.example.collegealart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.collegealart.data.table.AlertTable
import com.example.collegealart.data.viewModel.AlertViewModel
import com.example.collegealart.navigation.BottomBar
import com.example.collegealart.ui.theme.CollegeAlartTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    companion object{
        lateinit var alertViewModel :AlertViewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        alertViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
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
            if(!alerts.value.isNullOrEmpty())
            scheduleEventNotifications(this, alerts.value!!)
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
fun scheduleEventNotifications(context: Context, alertList: List<AlertTable>) {
    val eventTimes = alertList.mapNotNull { alert ->
        parseDateTime(alert.date, alert.time)
    }

    val workRequest = OneTimeWorkRequestBuilder<EventNotificationWorker>()
        .setInputData(
            Data.Builder()
                .putLongArray("event_times", eventTimes.toLongArray()) // تمرير القائمة بعد تحويلها
                .build()
        )
        .setInitialDelay(1, TimeUnit.MINUTES) // يتم التحقق كل دقيقة
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
class EventNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val eventTimes = inputData.getLongArray("event_times") ?: return Result.failure()
        val currentTime = System.currentTimeMillis()

        for (eventTime in eventTimes) {
            if (isSameMinute(eventTime, currentTime)) {
                sendNotification(eventTime)
            }
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
                calendar1[Calendar.MINUTE] == calendar2[Calendar.MINUTE] // التحقق بالدقائق فقط
    }

    private fun sendNotification(eventTime: Long) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "event_channel")
            .setContentTitle("Event Reminder")
            .setContentText("It's time for your event at ${DateFormat.format("hh:mm a", eventTime)}!")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(eventTime.hashCode(), notification)
    }
}



