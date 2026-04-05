package com.f1forhelp.ovo

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import com.f1forhelp.ovo.data.BleedEvent
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.menu.AppNav
import com.f1forhelp.ovo.notifications.scheduleCycleNotification
import com.f1forhelp.ovo.notifications.showNotification

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private val CHANNEL_ID = "cycle_channel"

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ENABLE to set navigation bar to black at the bottom.
        // (actual set color happens below in setContent)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        //region App Stuff
        BleedEvent.initDb(this)
        Cycle.initDb(this)

        val bleedEvents = BleedEvent.getAll()
        bleedEvents.forEach {
            println("Bleed Event: ${it.epochMillis}")
        }
        val cycles = Cycle.getAll()
        cycles.forEach {
            println("Prediction Date: ${it.predictionDateMs}")
        }

        enableEdgeToEdge()
        setContent {
            SetNavigationBarColor(color = Color.Black, darkIcons = false)
            AppNav()
        }


        //region Notification Stuff
        createNotificationChannel()
        requestNotificationPermission()

        // Simulate prediction result (e.g., 5 days from now)
        //val predictedStartMs = System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000L

        scheduleCycleNotification(this, 5)
        //endregion
    }
    private fun createNotificationChannel() {
        val name = "Cycle Notifications"
        val descriptionText = "Notifications about predicted cycles"

        val manager = getSystemService(NotificationManager::class.java)
        manager.deleteNotificationChannel(CHANNEL_ID) // remove old one

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableVibration(true)
            vibrationPattern = longArrayOf(
                0, 50,
                50, 50,
                50, 50,
                50, 50,
                50, 50,
                50, 50,
                50, 50,
                50, 50
            )
        }

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }
    }
}



