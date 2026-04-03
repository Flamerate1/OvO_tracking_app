package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.data.toDaysDouble

@Composable
fun TopButtons(navController: NavController) {
    // Top Buttons Row
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .statusBarsPadding(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PredictionText()

        Spacer(modifier=Modifier.size(10.dp))

        Button(
            onClick = { navController.popBackStack() },
            shape = CircleShape,
            modifier = Modifier.size(56.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}

@Composable
fun PredictionText() {
    val cycle = Cycle.getMostRecent()

    val predictedNext = cycle.predictedNextStartMs
    val nowMs = System.currentTimeMillis()
    //val msLeft = (predictedNext ?: 0L) - nowMs
    val msLeft = predictedNext - nowMs

    val daysLeft = msLeft.toDaysDouble()
    val daysLeftString = "%.1f".format(daysLeft)

    val madDays = cycle.madLength.toDaysDouble()
    val madDaysString = "%.1f".format(madDays)
    Text("Next bleed event in $daysLeftString±$madDaysString days")
}