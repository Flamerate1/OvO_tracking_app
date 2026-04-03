package com.f1forhelp.ovo.menu.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun InlineTimeDisplay(
    onRecord: (Long) -> Unit) {
    // States
    var month by remember { mutableStateOf(1) }
    var day by remember { mutableStateOf(1) }
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }

    val timezones = listOf("US/Eastern", "Asia/Shanghai", "Asia/Tokyo")
    var timezone by remember { mutableStateOf(timezones[0]) }
    val tzList = listOf(
        "EST" to "US/Eastern",
        "CST" to "US/Central",
        "JST" to "Asia/Tokyo"
    )
    var tzExpanded by remember { mutableStateOf(false) }
    var selectedShort by remember { mutableStateOf("EST") }
    var selectedFull by remember { mutableStateOf("US/Eastern") }


    fun updateToSystemTime() {
        val now = ZonedDateTime.now(ZoneId.of(timezone))
        month = now.monthValue
        day = now.dayOfMonth
        hour = now.hour
        minute = now.minute
    }

    LaunchedEffect(Unit) { updateToSystemTime() }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom, // push content to bottom
        modifier = Modifier.padding(16.dp)
            .fillMaxHeight()  // must fill available height!
            .navigationBarsPadding(), // lifts everything above system buttons
    ) {
        // Row for month/day/hour/minute
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Reset fields to system time
            ResetTimeFieldsButton { updateToSystemTime() }

            // Month
            StepperNumber(value = month, onValueChange = { month = it }, min = 1, max = 12)
            Text(" / ", style = MaterialTheme.typography.bodyLarge)

            // Day
            StepperNumber(value = day, onValueChange = { day = it }, min = 1, max = 31)
            Text(" / ", style = MaterialTheme.typography.bodyLarge)

            // Hour
            StepperNumber(value = hour, onValueChange = { hour = it }, min = 0, max = 23)
            Text(" : ", style = MaterialTheme.typography.bodyLarge)

            // Minute
            StepperNumber(value = minute, onValueChange = { minute = it }, min = 0, max = 59)

            // Timezone dropdown
            TimezoneDropdown(
                timezones = tzList,
                selectedShort = selectedShort,
                onSelected = { short, full ->
                    selectedShort = short
                    selectedFull = full
                    // Use selectedFull for ZonedDateTime computations
                }
            )

            Spacer(modifier = Modifier.width(4.dp))

            RecordEventWithConfirmation(month, day, hour, minute, selectedFull, onRecord, LocalContext.current)

        } // end row
    } // end column
}

@Composable
fun ResetTimeFieldsButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier.size(56.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Restart",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun StepperNumber(value: Int, onValueChange: (Int) -> Unit, min: Int, max: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Up button
        Button(
            onClick = { if (value < max) onValueChange(value + 1) },
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) { Text("▲") }

        // Number display
        Text("$value", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(4.dp))

        // Down button
        Button(
            onClick = { if (value > min) onValueChange(value - 1) },
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp)
        ) { Text("▼") }
    }
}

@Composable
fun TimezoneDropdown(
    timezones: List<Pair<String, String>>, // short -> full
    selectedShort: String,
    onSelected: (short: String, full: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(onClick = { expanded = !expanded }) {
            Text(selectedShort)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            timezones.forEach { (short, full) ->
                DropdownMenuItem(
                    text = { Text(full) }, // show full name in the menu
                    onClick = {
                        onSelected(short, full)
                        expanded = false
                    }
                )
            }
        }
    }
}