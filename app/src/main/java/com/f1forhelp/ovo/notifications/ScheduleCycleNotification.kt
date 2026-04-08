package com.f1forhelp.ovo.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.*
import com.f1forhelp.ovo.NotificationObject
import com.f1forhelp.ovo.data.Cycle
import java.util.concurrent.TimeUnit

/*
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun ScheduleCycleNotification(context: Context, notificationObject: NotificationObject, cycle: Cycle) {
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
}*/
/*
@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun scheduleCycleNotificationOld(context: Context, predictedStartMs: Long) {
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
*/