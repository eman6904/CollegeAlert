package com.example.collegealart

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.collegealart.navigation.BottomBar
import com.example.collegealart.ui.theme.CollegeAlartTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CollegeAlartTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = true
                //to change status bar color
                systemUiController.setStatusBarColor(
                    color = Color.White,
                    darkIcons = useDarkIcons
                )
                BottomBar(navController = rememberNavController())
            }
            // محاكاة حدث في المستقبل (بعد 5 ثواني من وقت التشغيل)
//            val eventTime = System.currentTimeMillis() + 100000 // بعد 5 ثواني
//            createNotificationChannel(this)
//            scheduleEventNotification(this, eventTime)
        }
    }
}

fun scheduleEventNotification(context: Context, eventTime: Long) {
    val workRequest = OneTimeWorkRequestBuilder<EventNotificationWorker>()
        .setInitialDelay(
            eventTime - System.currentTimeMillis(),
            TimeUnit.MILLISECONDS
        )  // تأخير العمل حتى وقت الحدث
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
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

// Worker لإرسال الإشعار
class EventNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    private fun sendNotification() {
        // بناء الإشعار
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "event_channel")
            .setContentTitle("Event Notification")
            .setContentText("It's time for your event!")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) // لتشغيل الصوت
            .build()

        notificationManager.notify(1, notification)
    }
}
