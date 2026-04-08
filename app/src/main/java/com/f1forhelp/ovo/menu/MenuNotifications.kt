package com.f1forhelp.ovo.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import com.f1forhelp.ovo.NotificationObject
import com.f1forhelp.ovo.NotificationType
import com.f1forhelp.ovo.SettingsStore
import com.f1forhelp.ovo.SettingsStore.GeneralSettings
import com.f1forhelp.ovo.SettingsStore.NotificationSettings
import com.f1forhelp.ovo.menu.main.TimezoneDropdown

@Composable
fun MenuNotifications(navController: NavController) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        TopButtons(navController)
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable Notifications", modifier = Modifier.weight(1f))
                //Switch(checked = NotificationSettings.enabled.value, onCheckedChange = { NotificationSettings.enabled.value = it })
                Switch(checked = NotificationSettings.enabled.value, onCheckedChange = { NotificationSettings.setEnabled(it) })
            }
            Spacer(modifier = Modifier.height(16.dp))

            val tzList = listOf(
                "EST" to "US/Eastern",
                "CST" to "US/Central",
                "JST" to "Asia/Tokyo"
            )
            var selectedShort by remember { mutableStateOf("EST") }
            var selectedFull by remember { mutableStateOf("US/Eastern") }

            Column(
                modifier = Modifier
                    //.padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Notifications time window:", modifier = Modifier.weight(1f))
                    //Switch(checked = NotificationSettings.enabled.value, onCheckedChange = { NotificationSettings.enabled.value = it })
                    /*TimezoneDropdown(
                        timezones = tzList,
                        selectedShort = selectedShort,
                        onSelected = { short, full ->
                            selectedShort = short
                            selectedFull = full
                        }
                    )*/
                    TimezoneDropdown()
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier=Modifier.weight(1f)) { Text("Start:") }
                    Slider(
                        modifier=Modifier.weight(6f),
                        value = NotificationSettings.windowStart.value.toFloat(),
                        onValueChange = { newValue ->
                            //NotificationSettings.windowStart.value = newValue.toInt().coerceAtMost(NotificationSettings.windowEnd.value)
                            NotificationSettings.windowStart.value = newValue.toInt()
                        },
                        valueRange = 0f..1440f,
                        steps = 1440,
                        enabled = NotificationSettings.enabled.value
                    )
                    Box(modifier=Modifier.weight(1f)) { Text(formatTime(NotificationSettings.windowStart.value)) }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) { Text("End:") }
                    Slider(
                        modifier=Modifier.weight(6f),
                        value = NotificationSettings.windowEnd.value.toFloat(),
                        onValueChange = { newValue ->
                            //NotificationSettings.windowEnd.value = newValue.toInt().coerceAtLeast(NotificationSettings.windowStart.value)
                            NotificationSettings.windowEnd.value = newValue.toInt()
                        },
                        valueRange = 0f..1440f,
                        steps = 1440,
                        enabled = NotificationSettings.enabled.value
                    )
                    Box(modifier=Modifier.weight(1f)) { Text(formatTime(NotificationSettings.windowEnd.value)) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color = if (NotificationSettings.enabled.value) Color(0xFFE0E0E0) else Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(8.dp)
            ) {

                val notificationList = NotificationSettings.notifications

                Button(
                    onClick = {
                        NotificationSettings.add(NotificationObject(
                            "Testing",
                            true,
                            NotificationType.DAY_OF,
                            0.0
                        ))
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp),
                    contentPadding = PaddingValues(0.dp),
                    enabled = NotificationSettings.enabled.value
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Notification"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                notificationList.forEachIndexed { index, item ->
                    Button(
                        onClick = { /* open item settings */ },
                        enabled = NotificationSettings.enabled.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(item.name)
                    }
                }
            }
        }




        Button( onClick = {navController.navigate("bleedEventData")} ) { Text("BleedEvent Data") }
        Button( onClick = {navController.navigate("cycleData")} ) { Text("Cycle Data") }
    }
}

fun formatTime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return "%02d:%02d".format(h, m)
}