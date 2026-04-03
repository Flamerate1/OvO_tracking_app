package com.f1forhelp.ovo.data

import android.content.Context
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.f1forhelp.ovo.AppDatabase
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.Long

fun List<BleedEvent>.toCycleLengths(): List<Long> {
    if (this.size < 2) return emptyList()
    return this.zipWithNext { a, b -> b.epochMillis - a.epochMillis }
}

@Entity(tableName = "bleedEvents")
data class BleedEvent(
    @PrimaryKey val epochMillis: Long = 0L
) {
    companion object {
        val empty = BleedEvent()
        var db: AppDatabase? = null

        // Optional helper to initialize database safely
        fun initDb(context: Context) {
            if (db == null) {
                db = AppDatabase.getDatabase(context)
            }
        }
        fun getAll(): List<BleedEvent> {
            val events = db!!.bleedEventDao().getAll().sortedBy{it.epochMillis}
            return events
        }

        fun getMostRecent(): BleedEvent {
            val events = getAll()
            if (events.isEmpty()) {
                return empty
            }
            return events[events.size-1]
        }

        fun delete(epochMillis : Long) { db!!.bleedEventDao().deleteByEpoch(epochMillis)}

        // Observers
        private val observers = mutableListOf<() -> Unit>()
        fun notifyObservers() { observers.forEach { it.invoke() } }
        fun addObserver(observer: () -> Unit) { observers.add(observer) }
        fun removeObserver(observer: () -> Unit) { observers.remove(observer) }

        fun fromCsvLine(line: String): BleedEvent {
            val parts = line.split(",")

            // Parse the ISO-8601 string
            val zdt = ZonedDateTime.parse(parts[0], DateTimeFormatter.ISO_ZONED_DATE_TIME)
            val epochMillis = zdt.toInstant().toEpochMilli()

            // Create BleedEvent
            return BleedEvent(
                epochMillis = epochMillis,
                // map other fields here if needed, e.g., parts[1]...
            )
        }
    }

    enum class Format {
        EPOCH_MILLIS,
        ZONED_DATE_TIME
    }

    fun save() {
        db!!.bleedEventDao().insert(this)

        notifyObservers()
    }

    fun asZonedDateTime(zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
        return Instant.ofEpochMilli(epochMillis).atZone(zone)
    }

    fun asFormattedString(zone: ZoneId = ZoneId.systemDefault()): String {
        return this.asZonedDateTime(zone).toString()
    }
}



@Entity(tableName = "cycles")
data class Cycle(
    @PrimaryKey
    val predictionDateMs: Long = 0L,
    val startMs: Long? = 0L,
    val nextStartMs: Long? = 0L,
    val length: Long? = 0L,
    val valid: Boolean = false,

    val medianLength: Long? = 0L,
    val madLength: Long? = 0L,
    val validCycleCount: Int? = 0,

    val predictedNextStartMs: Long? = 0L,
    val predictedNextOvulationMs: Long? = 0L
) {
    companion object {
        val empty = Cycle()
        var db: AppDatabase? = null

        // Optional helper to initialize database safely
        fun initDb(context: Context) {
            if (db == null) {
                db = AppDatabase.getDatabase(context)
            }
        }

        fun generateFromBleedEvents(bleedEvents: List<BleedEvent>): Cycle {
            val predictionDateMs = System.currentTimeMillis()

            val bleedEvents = BleedEvent.getAll()
            val lengths = bleedEvents.toCycleLengths()
            val validLengths = CycleProcesses.validLengths(lengths)
            val validCycleCount = validLengths.size

            val median = CycleProcesses.median(validLengths)
            val mad = CycleProcesses.mad(validLengths, median)

            val startMs = bleedEvents[bleedEvents.size - 2].epochMillis
            val nextStartMs = bleedEvents[bleedEvents.size - 1].epochMillis
            val currentLength = nextStartMs - startMs

            if (currentLength != lengths[lengths.size - 1]) {
                Log.d("MyTag","currentLength is not equal to lengths[lengths.size-1]")
            }

            val valid = CycleProcesses.isLengthValid(currentLength)

            val predictedNextStartMs = CycleProcesses.predictedNextStartMs(nextStartMs, median)
            val predictedNextOvulationMs = CycleProcesses.predictedNextOvulationMs(predictedNextStartMs) // TODO calculate

            return Cycle(
                predictionDateMs = predictionDateMs,
                startMs = startMs,
                nextStartMs = nextStartMs,
                length = currentLength,
                valid = valid,

                medianLength = median,
                madLength = mad,
                validCycleCount = validCycleCount,

                predictedNextStartMs = predictedNextStartMs,
                predictedNextOvulationMs = predictedNextOvulationMs
            )
        }
        fun getAll(): List<Cycle> {
            val events = db!!.cycleDao().getAll().sortedBy{it.startMs}
            return events
        }

        fun getMostRecent(): Cycle {
            val cycles = getAll()
            if (cycles.isEmpty()) {
                return empty
            }
            return cycles[cycles.size-1]
        }

        fun delete(startMs : Long) { db!!.cycleDao().deleteByEpoch(startMs)}

    }

    fun save() {
        db!!.cycleDao().insert(this)
    }
}