package com.f1forhelp.ovo.menu

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.data.BleedEvent

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.f1forhelp.ovo.AppManager
import com.f1forhelp.ovo.data.Cycle
import java.time.ZoneId

import androidx.compose.foundation.background


@Composable
fun BleedEventList() {
    val context = LocalContext.current

    var events by remember { mutableStateOf(BleedEvent.getAll()) }

    var candidateBleedEvent by remember { mutableStateOf(BleedEvent.empty) }
    var showDeleteConfirmation by remember {mutableStateOf(false)}


    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(3.dp, Color.Gray)
            .statusBarsPadding()
    ) {
        items(events) { event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        candidateBleedEvent = event
                        showDeleteConfirmation = true
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(event.asFormattedString(ZoneId.of("America/New_York")))
            }
            HorizontalDivider()
        }
    }

    if (showDeleteConfirmation && candidateBleedEvent != Cycle.empty) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false; candidateBleedEvent = BleedEvent.empty},
            title = { Text("Confirm Deletion") },
            text = {
                Text("Are you sure you'd like to delete bleed event  on ${candidateBleedEvent.asFormattedString()}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    val string = candidateBleedEvent.asFormattedString(ZoneId.of("America/New_York"))
                    BleedEvent.delete(candidateBleedEvent.epochMillis)
                    events = BleedEvent.getAll()
                    AppManager.instance.popupMessage(context,"Deletion of $string successful")

                    candidateBleedEvent = BleedEvent.empty
                    showDeleteConfirmation = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false; candidateBleedEvent = BleedEvent.empty}) {
                    Text("No")
                }
            }
        )
    }
}

@Composable
fun CycleList() {
    val context = LocalContext.current
    var cycles by remember { mutableStateOf(Cycle.getAll()) }

    val horizontalScrollState = rememberScrollState()


    var candidateCycle by remember { mutableStateOf(Cycle.empty) }
    var showDeleteConfirmation by remember {mutableStateOf(false)}

    Row(
        modifier = Modifier
            .horizontalScroll(horizontalScrollState) // scroll the whole chart
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .width(1500.dp)
                .height(400.dp)
                .border(3.dp, Color.Gray)
                .statusBarsPadding()
        ) {
            // Header row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Calc. Date", modifier = Modifier.weight(1f))
                    Text("Start", modifier = Modifier.weight(1f))
                    Text("Next Start", modifier = Modifier.weight(1f))
                    Text("Length (Days)", modifier = Modifier.weight(1f))
                    Text("Valid", modifier = Modifier.weight(1f))
                    Text("Median (Days)", modifier = Modifier.weight(1f))
                    Text("MAD (Days)", modifier = Modifier.weight(1f))
                    Text("Count", modifier = Modifier.weight(1f))
                    Text("Pred. Next", modifier = Modifier.weight(1f))
                    Text("Pred. Ovulation", modifier = Modifier.weight(1f))
                }
                HorizontalDivider()
            }

            // Data rows
            items(cycles) { cycle ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            candidateCycle = cycle
                            showDeleteConfirmation = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val v = cycle.toViewableCycle()
                    Text(v.predictionDate, modifier = Modifier.weight(1f))
                    Text(v.start, modifier = Modifier.weight(1f))
                    Text(v.nextStart, modifier = Modifier.weight(1f))
                    Text(v.length, modifier = Modifier.weight(1f))
                    Text(v.valid, modifier = Modifier.weight(1f))
                    Text(v.medianLength, modifier = Modifier.weight(1f))
                    Text(v.madLength, modifier = Modifier.weight(1f))
                    Text(v.validCycleCount, modifier = Modifier.weight(1f))
                    Text(v.predictedNextStart, modifier = Modifier.weight(1f))
                    Text(v.predictedNextOvulation, modifier = Modifier.weight(1f))
                }
                HorizontalDivider()
            }
        }

        /*LazyColumn(
            modifier = Modifier
                .width(800.dp) // adjust to fit your widest row
                .height(400.dp)
                .border(3.dp, Color.Gray)
                .statusBarsPadding()
        ) {
            items(cycles) { cycle ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth() // ensure the row uses full LazyColumn width
                        .padding(8.dp)
                        .clickable {
                            candidateCycle = cycle
                            showDeleteConfirmation = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(cycle.asFormattedString(ZoneId.of("America/New_York")))
                    VerticalDivider()
                    Text("Hi some text")
                    VerticalDivider()
                    Text("Hi some more text")
                    VerticalDivider()
                    Text("Even more text")
                }
                HorizontalDivider()
            }
        }*/
    }
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                Cycle.generateFromMostRecent()
                cycles = Cycle.getAll()
                Toast.makeText(context, "Cycle Data Generated", Toast.LENGTH_SHORT).show()
            },
        ) {
            Text("Generate Cycle Data")
        }
    }


    if (showDeleteConfirmation && candidateCycle != Cycle.empty) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false; candidateCycle = Cycle.empty},
            title = { Text("Confirm Deletion") },
            text = {
                Text("Are you sure you'd like to delete cycle  on ${candidateCycle.asFormattedString()}?")
            },
            confirmButton = {
                TextButton(onClick = {
                    val string = candidateCycle.asFormattedString(ZoneId.of("America/New_York"))
                    Cycle.deleteByPredictionDateMs(candidateCycle.predictionDateMs)
                    cycles = Cycle.getAll()
                    AppManager.instance.popupMessage(
                        context,
                        "Deletion of $string successful"
                    )

                    candidateCycle = Cycle.empty
                    showDeleteConfirmation = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false; candidateCycle = Cycle.empty}) {
                    Text("No")
                }
            }
        )
    }
}
