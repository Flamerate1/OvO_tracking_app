package com.f1forhelp.ovo.data

import android.content.Context
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.f1forhelp.ovo.AppDatabase
import com.f1forhelp.ovo.SettingsStore
import com.f1forhelp.ovo.SettingsStore.CalculationSettings
import com.f1forhelp.ovo.data.Analysis
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.Long
import kotlin.math.abs

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

    val startMs: Long = 0L,
    val nextStartMs: Long = 0L,
    val length: Long = 0L,
    val valid: Boolean = false,

    val medianLength: Long = 0L,
    val madLength: Long = 0L,
    val validCycleCount: Int = 0,

    val predictedNextStartMs: Long = 0L,
    val predictedNextOvulationMs: Long = 0L
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

        fun processIterativePredictions(bleedEvents: List<BleedEvent> = BleedEvent.getAll()) {
            // Calculate Overall MAD
            val lengths = BleedEvent.getAll().toCycleLengths()
            val mad = CycleProcesses.computeEstimate(lengths).margin


            for (i in 0 until bleedEvents.size - 1) {
                // Slice from beginning up to current
                val currentEvents = bleedEvents.subList(0, i + 2) // i + 2 end index is exclusive
                val newCycle = generateFromBleedEvents(currentEvents)


                // Compute Analysis
                if (getMostRecent() != empty) {
                    val prevCycle = getMostRecent()
                    val newAnalysis = Analysis.new(newCycle.startMs, prevCycle.predictedNextStartMs, mad).save()
                }
                newCycle.save()

                //cycles.add(newCycle)
            }

            //return cycles
        }

        fun generateFromMostRecent() {
            // Create new cycle data
            val events = BleedEvent.getAll()
            generateFromBleedEvents(events).save()
        }

        fun generateFromBleedEvents(bleedEvents: List<BleedEvent>): Cycle {
            val predictionDateMs = System.currentTimeMillis()

            val lengths = bleedEvents.toCycleLengths()

            Log.d("calculations",CalculationSettings.toString())

            /*
                Step 1 filtering process timeline:
                lengths -> sanityFiltered -> outlierFiltered -> validLengths
            */

            var validLengths = lengths
            if (CalculationSettings.useSanityFilter.value) validLengths = CycleProcesses.sanityFilter(
                lengths,
                CalculationSettings.sanityFilterMinDays.intValue.toMillisLong(),
                CalculationSettings.sanityFilterMaxDays.intValue.toMillisLong()
            )
            if (CalculationSettings.useOutlierFilter.value) validLengths = CycleProcesses.outlierFilter(
                validLengths,
                CalculationSettings.exclusionDeviationCap.doubleValue
            )
            val validCycleCount = validLengths.size

            // Compute the median and mad estimate depending on weighted status.
            val estimate: Estimate = if (CalculationSettings.useWeighted.value) {
                val inclusionCap = CalculationSettings.weightedInclusionCap.intValue
                val includedLengths = validLengths.takeLast(inclusionCap)
                Log.d("calculations",includedLengths.size.toString())
                CycleProcesses.computeWeightedEstimate(includedLengths, CalculationSettings.halfLife.doubleValue)
            } else {
                val inclusionCap = CalculationSettings.inclusionCap.intValue
                val includedLengths = validLengths.takeLast(inclusionCap)
                Log.d("calculations",includedLengths.size.toString())
                CycleProcesses.computeEstimate(includedLengths)
            }


            val startMs = bleedEvents[bleedEvents.size - 2].epochMillis
            val nextStartMs = bleedEvents[bleedEvents.size - 1].epochMillis
            val currentLength = nextStartMs - startMs

            if (currentLength != lengths[lengths.size - 1]) {
                Log.d("MyTag","currentLength is not equal to lengths[lengths.size-1]")
            }

            val valid = CycleProcesses.isLengthValid(currentLength)

            val predictedNextStartMs = CycleProcesses.predictedNextStartMs(nextStartMs, estimate.center)
            val predictedNextOvulationMs = CycleProcesses.predictedNextOvulationMs(predictedNextStartMs)

            return Cycle(
                predictionDateMs = predictionDateMs,
                startMs = startMs,
                nextStartMs = nextStartMs,
                length = currentLength,
                valid = valid,

                medianLength = estimate.center,
                madLength = estimate.margin,
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

        fun deleteByStartMs(startMs : Long) { db!!.cycleDao().deleteByEpoch(startMs)}
        fun deleteByPredictionDateMs(predictionDateMs : Long) { db!!.cycleDao().deleteByPredictionDateMs(predictionDateMs)}

    }

    fun save() {
        db!!.cycleDao().insert(this)
    }

    fun asZonedDateTime(zone: ZoneId = ZoneId.systemDefault()): ZonedDateTime {
        return Instant.ofEpochMilli(predictionDateMs).atZone(zone)
    }

    fun asFormattedString(zone: ZoneId = ZoneId.systemDefault()): String {
        return this.asZonedDateTime(zone).toString()
    }

    fun toViewableCycle(): ViewableCycle {
        return ViewableCycle(
            predictionDate = predictionDateMs.toFormattedDate(),

            start = startMs.toFormattedDate(),
            nextStart = nextStartMs.toFormattedDate(),
            length = length.toDayString(),
            valid = valid.toString(),

            medianLength = medianLength.toDayString(),
            madLength = madLength.toDayString(),
            validCycleCount = validCycleCount.toString(),

            predictedNextStart = predictedNextStartMs.toFormattedDate(),
            predictedNextOvulation = predictedNextOvulationMs.toFormattedDate()
        )
    }
}

fun Long.toDayString(): String {
    val days = this.toDouble() / (1000 * 60 * 60 * 24)
    return String.format("%.1f", days)
}

fun Long.toFormattedDate(zone: ZoneId = ZoneId.of("America/New_York")): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(zone)
    return formatter.format(instant)
}

data class ViewableCycle(
    val predictionDate: String,
    val start: String, val nextStart: String, val length: String, val valid: String,
    val medianLength: String, val madLength: String, val validCycleCount: String,
    val predictedNextStart: String, val predictedNextOvulation: String
)

@Entity(tableName = "analyses")
data class Analysis(
    @PrimaryKey
    val id: Int = 0,
    val cycleStartMs: Long = 0L,
    val prevCyclePredictedNextStart: Long = 0L,
    val error: Long = 0L, // = previousCycle.predictedNextStart - nextStartMs
    val normalizedAbsError: Double = 0.0, // = abs(error) / madLength
    val runningAvgNormalizedError: Double = 0.0 // = average(normalizedError of all cycles)
) {
    companion object {
        fun new(cycleStartMs: Long, prevCyclePredictedNextStart: Long, mad: Long): Analysis {
            val error = prevCyclePredictedNextStart - cycleStartMs

            val normalizedAbsError = abs(error.toDouble()) / mad

            val allNormalizedAbsError = getAllNormalizedAbsError() + normalizedAbsError
            val runningAvgNormalizedError = allNormalizedAbsError.average()

            return Analysis(
                nextId(), cycleStartMs, prevCyclePredictedNextStart,
                error, normalizedAbsError, runningAvgNormalizedError
            )
        }
        var lastId = 0 // Used in creating the data object id.
        fun nextId(): Int = ++lastId

        val empty = Analysis()
        var db: AppDatabase? = null
        // Optional helper to initialize database safely
        fun initDb(context: Context) {
            if (db == null) {
                db = AppDatabase.getDatabase(context)
            }
        }
        fun getAll(): List<Analysis> {
            val analyses = db!!.analysisDao().getAll().sortedBy{it.id}
            return analyses
        }
        fun getAllNormalizedAbsError(): List<Double> {
            val analyses = db!!.analysisDao().getAll().sortedBy{it.id}
            val list = analyses.map { it.normalizedAbsError }
            return list
            //return list.toMutableList()
        }
        fun deleteAll() = db!!.analysisDao().deleteAll()

    }
    fun save() {
        db!!.analysisDao().insert(this)
    }

    fun toViewableAnalysis(): ViewableAnalysis {
        return ViewableAnalysis(
            id = id.toString(),

            cycleStart = cycleStartMs.toFormattedDate(),
            prevCyclePredictedNextStart = prevCyclePredictedNextStart.toFormattedDate(),

            error = error.toString(),
            normalizedAbsError = "%.2f".format(normalizedAbsError),
            runningAvgNormalizedError = "%.2f".format(runningAvgNormalizedError)
        )
    }
}

data class ViewableAnalysis(
    val id: String, // From Int. Normal
    val cycleStart: String, // From Long. Show as DateTime
    val prevCyclePredictedNextStart: String, // From Long. Show as DateTime
    val error: String, // From Long. Normal
    val normalizedAbsError: String, // From Double. Normal + 2 decimal places
    val runningAvgNormalizedError: String // From Double. Normal + 2 decimal places
)