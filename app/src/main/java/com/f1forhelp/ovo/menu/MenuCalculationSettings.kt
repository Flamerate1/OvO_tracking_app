package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MenuCalculationSettings(navController: NavController) {
    Column {
        Text("Screen A")

        Button(
            onClick = {
                navController.navigate("screenB")
            }
        ) {
            Text("Go to Screen B")
        }
    }
}