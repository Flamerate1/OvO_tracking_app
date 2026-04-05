package com.f1forhelp.ovo

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class NotificationObject(
    val name: String
)

object SettingsStore {
    private const val PREFS_NAME = "prefs"
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    // In-memory cache
    //private val _notifications = mutableStateListOf<NotificationObject>()
    //val notifications: List<NotificationObject> get() = _notifications

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString("scheduled_notifications", null)

        val type = object : TypeToken<List<NotificationObject>>() {}.type
        val list: List<NotificationObject> = gson.fromJson(json, type) ?: emptyList()
        NotificationSettings.notifications.clear()
        NotificationSettings.notifications.addAll(list)
    }

    object GeneralSettings {

    }
    object NotificationSettings {
        val notifications = mutableStateListOf<NotificationObject>()
    }
    object CalculationSettings {

    }
}