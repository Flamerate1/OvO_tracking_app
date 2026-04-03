package com.f1forhelp.ovo.menu

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.data.BleedEvent

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.f1forhelp.ovo.AppManager
import com.f1forhelp.ovo.data.Cycle
import java.time.ZoneId


@Composable
fun BleedEventList() {
    val context = LocalContext.current

    var events by remember { mutableStateOf(BleedEvent.getAll()) }

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
                        val string = event.asFormattedString(ZoneId.of("America/New_York"))
                        BleedEvent.delete(event.epochMillis)

                        events = BleedEvent.getAll()

                        AppManager.instance.popupMessage(context,"Deletion of $string successful")
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(event.asFormattedString(ZoneId.of("America/New_York")))
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun CycleList() {
    val context = LocalContext.current

    var cycles by remember { mutableStateOf(Cycle.getAll()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(3.dp, Color.Gray)
            .statusBarsPadding()
    ) {
        items(cycles) { cycle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        val string = cycle.asFormattedString(ZoneId.of("America/New_York"))
                        Cycle.deleteByPredictionDateMs(cycle.predictionDateMs)

                        cycles = Cycle.getAll()

                        AppManager.instance.popupMessage(context,"Deletion of $string successful")
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(cycle.asFormattedString(ZoneId.of("America/New_York")))
            }
            HorizontalDivider()
        }
    }
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),         // take full width
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                Cycle.generateFromMostRecent()
                cycles = Cycle.getAll()
                Toast.makeText(context, "Bleed Events Successfully Imported", Toast.LENGTH_SHORT).show()
            },
        ) {
            Text("Generate Cycle Data")
        }
    }
}