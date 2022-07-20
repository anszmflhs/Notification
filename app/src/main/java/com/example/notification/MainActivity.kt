package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var notificationManager: NotificationManager? = null
    private val channel_id = "channel_1"

    private lateinit var binding: ActivityMainBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channel_id, "Countdown", "Notif When Countdown End")

        binding.btnStart.setOnClickListener {
            countDownTimer.start()
        }

        countDownTimer = object : CountDownTimer(1000,1000) {
            override fun onTick(millisUntilFinish: Long) {
                binding.timer.text = getString(R.string.time_reamining,millisUntilFinish / 1000)
            }

            override fun onFinish() {
                displayNotification()
            }

        }
    }

    private fun displayNotification() {
        val notificationId = 45
        val intent = Intent(this,ResultActivity::class.java)
        val pendingintent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this,channel_id)
            .setContentTitle("Countdown Timer")
            .setContentText("Timer End")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingintent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager?.notify(notificationId,notification)
    }

    private fun createNotificationChannel(id : String, name : String, channelDescription : String) {
        // Validasi SDK notif akan dibuat jika SDK 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}
//infp-t