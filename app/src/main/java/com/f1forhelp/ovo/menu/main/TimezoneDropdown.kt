package com.f1forhelp.ovo.menu.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.SettingsStore
import com.f1forhelp.ovo.SettingsStore.GeneralSettings

@Composable
fun TimezoneDropdown(
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = !expanded },
            enabled = enabled,
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp) // reduces padding
        ) {
            Text(GeneralSettings.chosenTzShort, fontFamily = FontFamily.Monospace)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            GeneralSettings.timeZoneList.forEachIndexed { index, (short, full) ->
                DropdownMenuItem(
                    text = { Text(full) }, // show full name in the menu
                    onClick = {
                        GeneralSettings.chosenTimeZone.intValue = index
                        GeneralSettings.save()
                        expanded = false
                    }
                )
            }
        }
    }
}