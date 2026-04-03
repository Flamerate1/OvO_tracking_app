package com.f1forhelp.ovo.menu

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.data.BleedEvent
import com.f1forhelp.ovo.data.Cycle
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.ZoneId

@Composable
fun MenuImport(navController: NavController) {
    val context = LocalContext.current
    val bleedEvents = remember { mutableStateListOf<BleedEvent>() }

    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                // This is the CSV file Uri
                handleCsvUri(context, uri, bleedEvents)
            }
        }
    )

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        TopButtons(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),         // take full width
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { csvPickerLauncher.launch(arrayOf("text/*")) }
            ) {
                Text("Import CSV Data")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {}
            ) {
                Text("Text Field Input")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)  // adjust height as needed
                .border(3.dp, Color.Gray)
                .statusBarsPadding()
        ) {
            items(bleedEvents) { event ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(event.asFormattedString(ZoneId.of("America/New_York")))
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
                    bleedEvents.forEach { it.save() } // save each BleedEvent into the SQL database
                    bleedEvents.clear() // Clear the mutable list.

                    // Create new cycle data
                    Cycle.generateFromMostRecent()

                    Toast.makeText(context, "Bleed Events Successfully Imported", Toast.LENGTH_SHORT).show()
                },
                enabled = bleedEvents.isNotEmpty()
            ) {
                Text("Confirm Import")
            }
        }

    }
}


fun handleCsvUri(context: Context, uri: Uri, bleedEventsList: MutableList<BleedEvent>) {
    //val context = LocalContext.current
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.forEachLine { line ->
            // Parse each line into BleedEvent
            val bleedEvent = BleedEvent.fromCsvLine(line)

            bleedEventsList.add(bleedEvent)

            println("Bleed event: $bleedEvent.epochMillis")
        }

    }
}