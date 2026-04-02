package com.f1forhelp.ovo.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.OutputStreamWriter
import java.io.IOException
import java.time.ZonedDateTime

object CsvManager {
    fun saveBleedEvents(context: Context) {
        val folder = context.getExternalFilesDir(null)
        if (!folder!!.exists()) folder.mkdir()
        val file = File(folder, "bleedEvents.csv")

        writeBleedEventsToFile(context, file)

        // Now copy file for backups
        val backupsFolder = File(context.getExternalFilesDir(null), "backups")
        if (!backupsFolder.exists()) backupsFolder.mkdir()
        val destFile = File(backupsFolder, "bleedEvents_${System.currentTimeMillis()}.csv")

        // Copy contents
        file.inputStream().use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun writeBleedEventsToFile(context: Context, file: File) {
        val db = context.openOrCreateDatabase("ovo.db", Context.MODE_PRIVATE, null)

        val cursor = db.rawQuery("SELECT * FROM bleedEvents", null)

        file.printWriter().use { out ->
            // Write header dynamically
            val columnNames = cursor.columnNames
            out.println(columnNames.joinToString(","))

            // Write rows
            while (cursor.moveToNext()) {
                val row = columnNames.map { column ->
                    cursor.getString(cursor.getColumnIndexOrThrow(column))
                }
                out.println(row.joinToString(","))
            }
        }

        cursor.close()
        db.close()
    }


}