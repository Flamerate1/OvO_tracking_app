package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.SettingsStore.NotificationSettings
import com.f1forhelp.ovo.notifications.NotificationService

@Composable
fun MenuCalculations(navController: NavController) {
    val context = LocalContext.current
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
                //Text("Enable Notifications", modifier = Modifier.weight(1f))
                Button(onClick = { NotificationService.scheduleAllNotifications(context) }) { Text("Reset Scheduled Notifications") }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable Notifications", modifier = Modifier.weight(1f))
                Switch(
                    checked = NotificationSettings.enabled.value,
                    onCheckedChange = { NotificationSettings.setEnabled(context, it) })
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}