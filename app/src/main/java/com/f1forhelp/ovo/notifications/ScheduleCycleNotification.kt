package com.f1forhelp.ovo.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.*
import java.util.concurrent.TimeUnit

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun scheduleCycleNotification(context: Context, predictedStartMs: Long) {
    /*val notifyAtMs = predictedStartMs - TimeUnit.DAYS.toMillis(2)
    val delay = notifyAtMs - System.currentTimeMillis()

    if (delay <= 0) {
        showNotification(context)
        return
    }

    val request = OneTimeWorkRequestBuilder<CycleNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.SECONDS)
        .build()*/

    val request = OneTimeWorkRequestBuilder<CycleNotificationWorker>()
        .setInitialDelay(predictedStartMs, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "cycle_notification",
        ExistingWorkPolicy.REPLACE,
        request
    )
}