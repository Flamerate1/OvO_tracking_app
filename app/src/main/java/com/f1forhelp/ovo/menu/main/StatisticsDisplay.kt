package com.f1forhelp.ovo.menu.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsDisplay() {
    val medianDays = 25.4
    val madDays = 0.5
    val predictedOvulationDateString = "Jan 0th"
    val predictedBleedEventDateString = "Jan 0th"

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                //.weight(1f)
                //.aspectRatio(1f) // make it square
                .padding(10.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                //.height(200.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Median length of cycle is ${medianDays}±${madDays} days")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Ovulation predicted for $predictedOvulationDateString")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Next bleed event predicted for $predictedBleedEventDateString")
            }
        }

    }
}