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

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import com.f1forhelp.ovo.data.Analysis
import com.f1forhelp.ovo.notifications.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //private val CHANNEL_ID = "cycle_channel"

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SettingsStore.init(this)

        // ENABLE to set navigation bar to black at the bottom.
        // (actual set color happens below in setContent)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        //region App Stuff
        BleedEvent.initDb(this)
        Cycle.initDb(this)
        Analysis.initDb(this)

        val bleedEvents = BleedEvent.getAll()
        bleedEvents.forEach {
            println("Bleed Event: ${it.epochMillis}")
        }
        val cycles = Cycle.getAll()
        cycles.forEach {
            println("Prediction Date: ${it.predictionDateMs}")
        }
        val analyses = Analysis.getAll()
        analyses.forEach {
            println("Analysis Date: ${it.id}")
        }

        enableEdgeToEdge()
        setContent {
            SetNavigationBarColor(color = Color.Black, darkIcons = false)
            AppNav()
        }


        //region Notification Stuff
        NotificationService.init(this)
        requestNotificationPermission()
        //endregion
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



