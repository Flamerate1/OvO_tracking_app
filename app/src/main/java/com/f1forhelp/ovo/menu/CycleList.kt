package com.f1forhelp.ovo.menu

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.data.Cycle

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import com.f1forhelp.ovo.AppManager
import java.time.ZoneId


@Composable
fun CycleList() {
    val context = LocalContext.current
    val cycles = Cycle.getAll()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)  // adjust height as needed
            .border(3.dp, Color.Gray)
            .statusBarsPadding()
    ) {
        items(cycles) { cycle ->
            /*Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        val string = cycle.asFormattedString(ZoneId.of("America/New_York"))
                        Cycle.delete(cycle.epochMillis)
                        AppManager.instance.popupMessage(context,"Deletion of $string successful")
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(cycle.asFormattedString(ZoneId.of("America/New_York")))
            }*/
            HorizontalDivider()
        }
    }
}