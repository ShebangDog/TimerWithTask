package com.example.timerwithhandler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.timerwithhandler.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    private val CHANNEL_ID = "773"
    private var toNotificationBar = false

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        createNotificationChannel()
        with(binding) {
            viewModel.time.observe(this@MainActivity) {
                timer.text = it.toString()
                if (toNotificationBar) notifyTime(it)
            }

            startButton.setOnClickListener {
                viewModel.startTime()
            }

            stopButton.setOnClickListener {
                viewModel.stopTime()
            }

            startOnNotificationButton.setOnClickListener {
                viewModel.startTime()
                toNotificationBar = true
            }

            stopOnNotificationButton.setOnClickListener {
                viewModel.stopTime()
                toNotificationBar = false
            }

        }
    }


    private fun notifyTime(currentTime: Long) {
        val customView = createCustomNotificationLayout(currentTime)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setPriority(NotificationCompat.PRIORITY_DEFAULT)
            setCustomContentView(customView)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }

        with(NotificationManagerCompat.from(this@MainActivity)) {
            val notificationId = 1

            notify(notificationId, builder.build())
        }
    }

    private fun createCustomNotificationLayout(currentTime: Long): RemoteViews = RemoteViews(
        packageName, R.layout.layout_custom_notification
    ).apply {
        setTextViewText(R.id.title, "Test Notification")
        setTextViewText(R.id.timer, currentTime.toString())
        setImageViewResource(R.id.image, R.mipmap.ic_launcher)
    }

    private fun createNotificationChannel() {
        val name = CHANNEL_ID
        val descriptionText = "for testing"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
