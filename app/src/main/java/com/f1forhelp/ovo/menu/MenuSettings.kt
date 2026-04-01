package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuSettings(navController: NavController) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        TopButtons(navController)

        Spacer(modifier = Modifier.height(16.dp))

        //Button( onClick = {navController.navigate("notifications")} ) { Text("Notifications") }
        Button( onClick = {navController.navigate("import")} ) { Text("Import Data") }
        Button( onClick = {navController.navigate("backup")} ) { Text("Backup Data") }
    }
}

