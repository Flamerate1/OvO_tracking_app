package com.f1forhelp.ovo.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.data.toDaysDouble
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TopButtons(
    navController: NavController,
    buttonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    onClick: () -> Unit = {
        if (navController.previousBackStackEntry != null) {
        navController.popBackStack()
    }
}) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .statusBarsPadding()
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(28.dp)),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PredictionText()

        Spacer(modifier=Modifier.size(10.dp))

        Button(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                //imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                imageVector = buttonIcon,
                contentDescription = "Back"
            )
        }
    }

}

@Composable
fun PredictionText() {
    val cycle = Cycle.getMostRecent()
    if (cycle == Cycle.empty) {
        Text("No prediction data exists yet.")
        return
    }


    val predictedNext = cycle.predictedNextStartMs
    val nowMs = System.currentTimeMillis()
    val msLeft = predictedNext - nowMs

    val daysLeft = msLeft.toDaysDouble()
    val daysLeftString = "%.1f".format(daysLeft)

    val madDays = cycle.madLength.toDaysDouble()
    val madDaysString = "%.1f".format(madDays)
    Text("Next bleed event in $daysLeftString±$madDaysString days")
}

@Composable
fun NotificationListDialog(scheduled: List<String>, onDismiss: () -> Unit) {
    var open by remember { mutableStateOf(true) }
    if (open) {
        AlertDialog(
            onDismissRequest = {
                open = false
                onDismiss()
            },
            title = { Text("Scheduled Notifications") },
            text = {
                val scrollState = rememberScrollState()
                androidx.compose.foundation.layout.Column(
                    modifier = androidx.compose.ui.Modifier.verticalScroll(scrollState)
                ) {
                    scheduled.forEach { Text(it) }
                }
            },
            confirmButton = {
                Button(onClick = {
                    open = false
                    onDismiss()
                }) {
                    Text("OK")
                }
            }
        )
    }
}