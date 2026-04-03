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
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.data.toDaysDouble
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale


fun Int.toOrdinal(): String {
    return when {
        this % 100 in 11..13 -> "${this}th"
        this % 10 == 1 -> "${this}st"
        this % 10 == 2 -> "${this}nd"
        this % 10 == 3 -> "${this}rd"
        else -> "${this}th"
    }
}

@Composable
fun StatisticsDisplay() {

    val cycle = Cycle.getMostRecent()


    val medianDays = cycle.medianLength.toDaysDouble()
    val medianDaysString = "%.1f".format(medianDays)

    val madDays = cycle.madLength.toDaysDouble()
    val madDaysString = "%.1f".format(madDays)


    val zone = ZoneId.of("America/New_York")

    val predictedBleedZdt = Instant.ofEpochMilli(cycle.predictedNextStartMs).atZone(zone)
    val bleedMonth = predictedBleedZdt.month.getDisplayName(TextStyle.SHORT, Locale.US).toString()
    val bleedDay = predictedBleedZdt.dayOfMonth.toOrdinal()

    val predictedOvulationZdt = Instant.ofEpochMilli(cycle.predictedNextOvulationMs).atZone(zone)
    val ovulationMonth = predictedOvulationZdt.month.getDisplayName(TextStyle.SHORT, Locale.US).toString()
    val ovulationDay = predictedOvulationZdt.dayOfMonth.toOrdinal()


    val predictedOvulationDateString = ovulationMonth + " " + ovulationDay
    val predictedBleedEventDateString = bleedMonth + " " + bleedDay

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
                Text("Median length of cycle is ${medianDaysString}±${madDaysString} days")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Ovulation predicted for $predictedOvulationDateString")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Next bleed event predicted for $predictedBleedEventDateString")
            }
        }

    }
}