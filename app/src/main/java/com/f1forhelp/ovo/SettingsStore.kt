package com.f1forhelp.ovo

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.f1forhelp.ovo.notifications.NotificationService

data class NotificationObject(
    val name: String, // Custom name if desired
    val enabled: Boolean, // Currently being used or not
    val type: NotificationType, // How the value is used
    val value: Double // Relative time before OR after predicted bleed event
)
enum class NotificationType {
    DAYS, MAD, DAY_OF, OVO_DAY
}

object SettingsStore {
    private const val PREFS_NAME = "prefs"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    object GeneralSettings {
        val timeZoneList = listOf(
            "🇺🇸 -5" to "US/Eastern", // US Emoji
            "🇺🇸 -6" to "US/Central", // US Emoji
            "🇯🇵 +9" to "Asia/Tokyo", // Japan Emoji
            "🇨🇳 +8" to "Asia/Shanghai" // China Emoji
        )
        var chosenTimeZone = mutableIntStateOf(0)
        val chosenTzShort: String
            get() = timeZoneList[chosenTimeZone.intValue].first
        val chosenTzFull: String
            get() = timeZoneList[chosenTimeZone.intValue].second
        fun save() {
            prefs.edit {
                putInt("chosenTimeZone", chosenTimeZone.intValue)
            }
        }
        fun load() {
            with (GeneralSettings) {
                chosenTimeZone.intValue = prefs.getInt("chosenTimeZone", 0)
            }
        }
    }
    object NotificationSettings {
        var enabled = mutableStateOf(false)
        var windowStart = mutableIntStateOf(0)
        var windowEnd = mutableIntStateOf(0)
        val notifications = mutableStateListOf<NotificationObject>()
        var scheduledNotifications:Set<String> = emptySet()
        fun updateScheduledNotifications(set: Set<String>) { scheduledNotifications = set; save() }


        fun add(newNotification: NotificationObject) {
            notifications.add(newNotification)
            save()
        }
        fun update(oldNotification: NotificationObject, newNotification: NotificationObject) {
            //val index = notifications.indexOf(oldNotification)
            val index = notifications.indexOfFirst { it === oldNotification }
            if (index != -1) notifications[index] = newNotification
            save()
        }
        fun update(
                old: NotificationObject,
                name: String = old.name,
                enabled: Boolean = old.enabled,
                type: NotificationType = old.type,
                value: Double = old.value
        ) {
            update(old, NotificationObject(name, enabled, type, value))
        }
        fun remove(oldNotification: NotificationObject) {
            //notifications.remove(oldNotification)
            notifications.removeAll { it === oldNotification }
            save()
        }
        //@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
        fun setEnabled(context: Context, isEnabled: Boolean) {
            enabled.value = isEnabled
            if (isEnabled) {
                NotificationService.scheduleAllNotifications(context)
            } else {
                NotificationService.clearAllNotifications(context)
            }

            save()
        }
        fun save() {
            val json = gson.toJson(notifications)
            prefs.edit {
                putString("notifications", json)
                putBoolean("enabled", enabled.value)
                putInt("windowStart", windowStart.intValue)
                putInt("windowEnd", windowEnd.intValue)
                putStringSet("scheduledNotifications", scheduledNotifications)
            }
        }
        fun load() {
            val json = prefs.getString("notifications", null)
            val type = object : TypeToken<List<NotificationObject>>() {}.type
            val list: List<NotificationObject> = gson.fromJson(json, type) ?: emptyList()
            with (NotificationSettings) {
                notifications.clear()
                notifications.addAll(list)
                notifications.forEach { Log.d("Notifications:",it.toString()) }
                enabled.value = prefs.getBoolean("enabled", false)
                windowStart.intValue = prefs.getInt("windowStart", 0)
                windowEnd.intValue = prefs.getInt("windowEnd", 0)

                scheduledNotifications = prefs.getStringSet("scheduledNotifications", emptySet()) ?: emptySet()
            }
        }
    }
    object CalculationSettings {
        fun save() {}
        fun load() {}
    }

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        GeneralSettings.load()
        NotificationSettings.load()
        CalculationSettings.load()
    }



}