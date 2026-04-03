package com.f1forhelp.ovo.menu.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.f1forhelp.ovo.data.BleedEvent
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

import androidx.compose.ui.graphics.painter.Painter
import com.f1forhelp.ovo.data.Cycle
import java.time.ZonedDateTime

data class CalendarDay(
    val day: String,          // e.g., 1..31
    val color: Color = Color.White,  // background color
    //var text: String? = null,    // optional text
    val text: String = "",    // optional text
    val icon: Painter? = null,    // optional icon
    val isToday: Boolean = false
)

fun processCalendarDays(
    rows: Int = 5,
    columns: Int = 7,
    bleedDay: BleedEvent,
    cycle: Cycle
): List<CalendarDay> {

    val zone = ZoneId.of("America/New_York")
    val bleedZdt = Instant.ofEpochMilli(bleedDay.epochMillis).atZone(zone)
    var current = bleedZdt

    var dayOfWeek = bleedZdt.dayOfWeek.value
    if (dayOfWeek == 7) {dayOfWeek = 0} // Make Sunday(7) into 0.

    current = current.minusDays(dayOfWeek.toLong())
    val now = ZonedDateTime.now()
    var defaultColor = Color.Gray
    val calendarDays = mutableListOf<CalendarDay>()

    repeat(rows * columns) {
        val dayOfMonth = current.dayOfMonth
        var day = dayOfMonth.toString()

        var text = when (dayOfMonth) {
            1 -> current.month.getDisplayName(TextStyle.SHORT, Locale.US).toString()
            else -> ""
        }

        var color = defaultColor
        var isToday = false
        if (current.toLocalDate() == now.toLocalDate()) {
            isToday = true
            defaultColor = Color.White
            color = defaultColor
        }
        if (current.toLocalDate() == bleedZdt.toLocalDate()) {
            color = Color.Red
        }
        calendarDays.add(CalendarDay(day = day, color = color, text = text, isToday = isToday))

        current = current.plusDays(1)
    }
    return calendarDays
}

@Composable
fun CalendarGrid(
) {
    val rows = 5
    val columns = 7

    val mostRecentBleedEvent = BleedEvent.getMostRecent()

    val mostRecentCycleData = Cycle.getMostRecent()
    val calendarDays = processCalendarDays(rows, columns, mostRecentBleedEvent, mostRecentCycleData)
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (day in daysOfWeek) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f), // square for alignment
                    contentAlignment = Alignment.Center
                ) {
                    Text(day, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (mostRecentBleedEvent == BleedEvent.empty) {

            Row(
                modifier = Modifier.fillMaxWidth().background(Color.Gray, shape = RoundedCornerShape(4.dp)),
                horizontalArrangement = Arrangement.spacedBy(4.dp),

            ) {
                Text("NO DATA PRESENT YET")
            }
            return
        }

        Spacer(modifier = Modifier.height(4.dp))

        val boxSpacing = 3.dp
        var i = 0
        for (r in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(boxSpacing)
            ) {
                for (c in 0 until columns) {
                    val d = calendarDays[i]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f) // make it square
                            .background(d.color, shape = RoundedCornerShape(boxSpacing))
                    ) {
                        Column {
                            Text(d.day)
                            Text(d.text)
                        }
                        if (d.isToday) { // add a property to CalendarDay for today
                        Box(
                            modifier = Modifier
                                .size(6.dp) // small dot
                                .background(Color.Red, shape = CircleShape)
                                .align(Alignment.BottomCenter)
                        )
                    }
                    }
                    i++
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}