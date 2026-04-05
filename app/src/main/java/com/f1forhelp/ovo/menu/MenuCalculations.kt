package com.f1forhelp.ovo.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun MenuCalculations(navController: NavController) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        TopButtons(navController)
    }
}