package com.f1forhelp.ovo.menu

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.AppDatabase
import com.f1forhelp.ovo.data.BleedEvent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.f1forhelp.ovo.AppManager
import java.time.ZoneId


@Composable
fun BleedEventList(/*events: List<BleedEvent>*/) {
    val context = LocalContext.current
    var events by remember { mutableStateOf(listOf<BleedEvent>()) }
    val observer = { events = BleedEvent.getAll() }
    DisposableEffect(Unit) {
        observer.invoke()
        BleedEvent.addObserver(observer)
        onDispose { BleedEvent.removeObserver(observer) }
    }

    // Scrollable table of events
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)  // adjust height as needed
            .border(3.dp, Color.Gray)
            .statusBarsPadding()
    ) {
        items(events) { event ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        // Handle click here
                        //println("Clicked event: ${event.id}") // or call a lambda
                        val string = event.asFormattedString(ZoneId.of("America/New_York"))
                        BleedEvent.delete(event.epochMillis)
                        AppManager.instance.popupMessage(context,"Deletion of $string successful")
                        observer.invoke()
                        //Toast.makeText(applicationContext, "Record successful", Toast.LENGTH_SHORT).show()
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Text("ID: ${event.id ?: "-"}")
                //Text("Epoch: ${event.epochMillis}", color = Color.Black)
                Text(event.asFormattedString(ZoneId.of("America/New_York")))
            }
            HorizontalDivider()
        }
    }
}