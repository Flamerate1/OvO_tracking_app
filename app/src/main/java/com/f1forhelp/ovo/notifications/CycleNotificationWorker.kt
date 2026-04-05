package com.f1forhelp.ovo.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.Worker
import androidx.work.WorkerParameters

class CycleNotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        showNotification(applicationContext)
        return Result.success()
    }
}