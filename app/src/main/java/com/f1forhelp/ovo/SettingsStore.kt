package com.f1forhelp.ovo

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.f1forhelp.ovo.SettingsStore.GeneralSettings.timeZoneList

data class NotificationObject(
    val name: String, // Custom name if desired
    val enabled: Boolean, // Currently being used or not
    val type: NotificationType, // How the value is used
    val value: Double // Relative time before OR after predicted bleed event
)
enum class NotificationType {
    DAYS, MAD, DAY_OF
}


object SettingsStore {
    private const val PREFS_NAME = "prefs"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    object GeneralSettings {
        val timeZoneList = listOf(
            "🇺🇸 -5" to "US/Eastern",
            "🇺🇸 -6" to "US/Central",
            "🇯🇵 +9" to "Asia/Tokyo",
            "🇨🇳 +8" to "Asia/Shanghai"
        )
        var chosenTimeZone = mutableIntStateOf(0)
        val chosenTzShort: String
            get() = timeZoneList[chosenTimeZone.intValue].first
        val chosenTzFull: String
            get() = timeZoneList[chosenTimeZone.intValue].second

    }
    object NotificationSettings {
        var enabled = mutableStateOf(false)
        var windowStart = mutableStateOf(0)
        var windowEnd = mutableStateOf(0)
        val notifications = mutableStateListOf<NotificationObject>()

        fun add(newNotification: NotificationObject) {
            notifications.add(newNotification)
            save()
        }
        fun setEnabled(isEnabled: Boolean) {
            enabled.value = isEnabled
            save()
        }
        fun save() {
            val json = gson.toJson(notifications)
            prefs.edit {
                putString("scheduled_notifications", json)
            }
        }
    }
    object CalculationSettings {

    }
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        /*
        val list = try {
            gson.fromJson<List<NotificationObject>>(json, type) // ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

         */
        val json = prefs.getString("scheduled_notifications", null)

        val type = object : TypeToken<List<NotificationObject>>() {}.type
        val list: List<NotificationObject> = gson.fromJson(json, type) ?: emptyList()
        /*if (list.isEmpty()) {
            NotificationSettings.notifications.addAll(listOf(
                NotificationObject("Template Notification1", false),
                NotificationObject("Template Notification2", true),
                NotificationObject("Template Notification3", false)
            ))
        }*/
        NotificationSettings.notifications.clear()
        NotificationSettings.notifications.addAll(list)
        NotificationSettings.notifications.forEach { Log.d("Notifications:",it.toString()) }
    }



}