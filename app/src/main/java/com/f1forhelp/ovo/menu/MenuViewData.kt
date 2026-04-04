package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun MenuViewData(navController: NavController) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        TopButtons(navController)
/*
        Spacer(modifier = Modifier.height(16.dp))

        Button( onClick = {navController.navigate("bleedEventData")} ) { Text("BleedEvent Data") }
        Button( onClick = {navController.navigate("cycleData")} ) { Text("Cycle Data") }

        Spacer(modifier = Modifier.height(32.dp))
*/
        DataViewChoice()
    }


}
@Composable
fun DataViewChoice() {
    val options = listOf("BleedEvent", "Cycle")
    val content = listOf<@Composable () -> Unit>(
        { BleedEventList() },
        { CycleList() }
    )

    var selected by remember { mutableStateOf(options[0]) }

    TextButtonRadioGroup(
        options = options,
        selectedOption = selected,
        onOptionSelected = { selected = it },
        contentForOption = content
    )
}

@Composable
fun TextButtonRadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    contentForOption: List<@Composable () -> Unit>
) {
    val defaultColor = MaterialTheme.colorScheme.primary
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Buttons row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Button(
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) defaultColor else Color.Gray
                    )
                ) {
                    Text(option)
                }
            }
        }

        // Display the content for the selected option
        val selectedIndex = options.indexOf(selectedOption)
        if (selectedIndex in contentForOption.indices) {
            contentForOption[selectedIndex]()
        }
    }
}