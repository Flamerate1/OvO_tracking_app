package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.f1forhelp.ovo.SettingsStore
import com.f1forhelp.ovo.SettingsStore.CalculationSettings
import com.f1forhelp.ovo.SettingsStore.NotificationSettings
import com.f1forhelp.ovo.notifications.NotificationService

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun MenuCalculations(navController: NavController) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        TopButtons(navController)
        Spacer(modifier = Modifier.height(16.dp))

        /*Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                //Text("Enable Notifications", modifier = Modifier.weight(1f))
                Button(
                    modifier=Modifier.fillMaxWidth(),
                    onClick = { NotificationService.scheduleAllNotifications(context) }) {Text("Reset Scheduled Notifications")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Enable Notifications", modifier = Modifier.weight(1f))
                Switch(
                    checked = NotificationSettings.enabled.value,
                    onCheckedChange = { NotificationSettings.setEnabled(context, it) })
            }
            Spacer(modifier = Modifier.height(16.dp))
        }*/
        CalculationSettingsMenu()
    }
}

@Composable
fun CalculationSettingsMenu() {
    Column(modifier = Modifier.padding(16.dp)) {

        // Double: exclusionDeviationCap
        var exclusionText by remember { mutableStateOf(CalculationSettings.exclusionDeviationCap.doubleValue.toString()) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Calculation Settings",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Int: inclusionCap
        var inclusionText by remember { mutableStateOf(CalculationSettings.inclusionCap.intValue.toString()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Inclusion Cap (Days)", modifier = Modifier.weight(1f))
            TextField(
                value = inclusionText,
                onValueChange = {
                    inclusionText = it
                    it.toIntOrNull()?.let { v ->
                        CalculationSettings.edit(inclusionCap = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Boolean: useSanityFilter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use Sanity Filter", modifier = Modifier.weight(1f))
            Switch(
                checked = CalculationSettings.useSanityFilter.value,
                onCheckedChange = {
                    CalculationSettings.edit(useSanityFilter = it)
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Int: sanityFilterMinDays
        var minText by remember { mutableStateOf(CalculationSettings.sanityFilterMinDays.intValue.toString()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sanity Min (Days)", modifier = Modifier.weight(1f))
            TextField(
                value = minText,
                onValueChange = {
                    minText = it
                    it.toIntOrNull()?.let { v ->
                        CalculationSettings.edit(sanityFilterMinDays = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Int: sanityFilterMaxDays
        var maxText by remember { mutableStateOf(CalculationSettings.sanityFilterMaxDays.intValue.toString()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Sanity Max (Days)", modifier = Modifier.weight(1f))
            TextField(
                value = maxText,
                onValueChange = {
                    maxText = it
                    it.toIntOrNull()?.let { v ->
                        CalculationSettings.edit(sanityFilterMaxDays = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Boolean: useOutlierFilter
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use Outlier Filter (Uses Exclusion Deviation Cap)", modifier = Modifier.weight(1f))
            Switch(
                checked = CalculationSettings.useOutlierFilter.value,
                onCheckedChange = {
                    CalculationSettings.edit(useOutlierFilter = it)
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Double: exclusionDeviationCap
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Exclusion Deviation Cap (MAD)", modifier = Modifier.weight(1f))
            TextField(
                value = exclusionText,
                onValueChange = {
                    exclusionText = it
                    it.toDoubleOrNull()?.let { v ->
                        CalculationSettings.edit(exclusionDeviationCap = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Spacer(modifier = Modifier.height(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Weighted MAD Calculations",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Boolean: useWeighted
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Use Weighted", modifier = Modifier.weight(1f))
            Switch(
                checked = CalculationSettings.useWeighted.value,
                onCheckedChange = {
                    CalculationSettings.edit(useWeighted = it)
                }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Int: weightedInclusionCap
        var weightedCapText by remember { mutableStateOf(CalculationSettings.weightedInclusionCap.intValue.toString()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Weighted Inclusion Cap", modifier = Modifier.weight(1f))
            TextField(
                value = weightedCapText,
                onValueChange = {
                    weightedCapText = it
                    it.toIntOrNull()?.let { v ->
                        CalculationSettings.edit(weightedInclusionCap = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Double: halfLife
        var halfLifeText by remember { mutableStateOf(CalculationSettings.halfLife.doubleValue.toString()) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Half Life", modifier = Modifier.weight(1f))
            TextField(
                value = halfLifeText,
                onValueChange = {
                    halfLifeText = it
                    it.toDoubleOrNull()?.let { v ->
                        CalculationSettings.edit(halfLife = v)
                    }
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}