package com.f1forhelp.ovo.menu.main

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SettingsDialogBox(
    onDismiss: () -> Unit,
    navController: NavController
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Settings") },
        text = {
            Column {
                TextButton(onClick = {
                    navController.navigate("notifications")
                    onDismiss()
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                    Text("Notifications")
                }

                TextButton(onClick = {
                    navController.navigate("calculations")
                    onDismiss()
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                    Text("Calculations")
                }

                TextButton(onClick = {
                    navController.navigate("import")
                    onDismiss()
                }) {
                    Text("Import Data")
                }

                TextButton(onClick = {
                    navController.navigate("backup")
                    onDismiss()
                }) {
                    Text("Backups")
                }

                TextButton(onClick = {
                    navController.navigate("viewData")
                    onDismiss()
                }) {
                    Text("View Data")
                }
            }
        },
        confirmButton = {}, // not needed
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}