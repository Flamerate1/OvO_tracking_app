package com.f1forhelp.ovo.data

import android.content.Context
import android.net.Uri
import java.io.OutputStreamWriter
import java.io.IOException
import java.time.ZonedDateTime

object CsvManager {
    fun exportBleedEventsToCsv(
        context: Context,
        events: List<ZonedDateTime>,
        uri: Uri
    ) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                events.forEach { date ->
                    writer.append(date.toString())
                    writer.append("\n")
                }
            }
        } ?: throw IOException("Failed to open output stream")
    }

    fun import() {

    }
}