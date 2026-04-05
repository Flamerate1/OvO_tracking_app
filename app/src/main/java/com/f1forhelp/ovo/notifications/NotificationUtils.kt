package com.f1forhelp.ovo.notifications

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun showNotification(context: Context) {
    val notification = NotificationCompat.Builder(context, "cycle_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Cycle Reminder")
        .setContentText("Your predicted cycle is approaching")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(1, notification)
}