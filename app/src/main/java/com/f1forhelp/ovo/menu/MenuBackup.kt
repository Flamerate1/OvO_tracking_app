package com.f1forhelp.ovo.menu

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.data.CsvManager
import java.io.File

@Composable
fun MenuBackup(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        TopButtons(navController)

        Spacer(modifier = Modifier.height(16.dp))

        var toggleState by remember { mutableStateOf(false) }

        /* Acceptable choices for choice buttons
        ToggleOption(
            label = "ZonedDateTime?",
            checked = toggleState,
            onCheckedChange = { toggleState = it }
        )
        TwoChoiceSwitch(
            label = "Export to: ",
            option1 = "ZonedDateTime",
            option2 = "EpochMillis",
            checked = toggleState,
            onCheckedChange = { toggleState = it }
        )*/

        val options = listOf("EpochMillis", "ZonedDateTime")
        var choice by remember { mutableStateOf(options[0]) }
        TwoChoiceRadio(
            label = "Choose Export Format:",
            options = options,
            selectedOption = choice,
            onOptionSelected = { choice = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 10.dp),         // take full width
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = { CsvManager.saveBleedEvents(context) }
            ) {
                Text("Backup Data")
            }


            val context = LocalContext.current

            // Launcher for "Save As" dialog
            val exportDbLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("application/x-sqlite3"),
                onResult = { uri ->
                    if (uri != null) {
                        exportDbToUri(context, uri)
                    }
                }
            )
            Button(modifier = Modifier.weight(1f),
                onClick = {
                    // Opens system picker, lets user pick file name/location
                    exportDbLauncher.launch("ovo.db")
                }) {
                Text("Export DB")
            }


            val fileName = "bleedEvents.csv"
            // Launcher for system "Save As" dialog
            val exportLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("*/*"), // allow any file type
                onResult = { uri ->
                    if (uri != null) {
                        copyFileToUri(context, fileName, uri)
                    }
                }
            )

            Button(modifier = Modifier.weight(1f),
                    onClick = {
                // Opens picker with suggested filename
                exportLauncher.launch(fileName)
            }) {
                Text("Export to CSV")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* Acceptable choices for choice buttons
@Composable
fun ToggleOption(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun TwoChoiceSwitch(
    label: String,
    option1: String,
    option2: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Text(text = label, modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(option1)
            Switch(checked = checked, onCheckedChange = onCheckedChange)
            Text(option2)
        }
    }
}
*/
@Composable
fun TwoChoiceRadio(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(label)
        Row {
            options.forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 16.dp)) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) }
                    )
                    Text(option)
                }
            }
        }
    }
}

// Copy ovo.db to chosen Uri
fun exportDbToUri(context: Context, uri: Uri) {
    try {
        val dbFile = context.getDatabasePath("ovo.db")
        context.contentResolver.openOutputStream(uri)?.use { output ->
            dbFile.inputStream().use { input ->
                input.copyTo(output)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Copies a file from app's parent directory to user-chosen Uri
fun copyFileToUri(context: Context, fileName: String, uri: Uri) {
    val sourceFile = File(context.getExternalFilesDir(null), fileName)
    if (!sourceFile.exists()) return

    try {
        context.contentResolver.openOutputStream(uri)?.use { output ->
            sourceFile.inputStream().use { input ->
                input.copyTo(output)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}