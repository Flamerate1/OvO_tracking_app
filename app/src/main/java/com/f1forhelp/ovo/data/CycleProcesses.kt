package com.f1forhelp.ovo.data

fun Long.toDaysDouble(): Double {
    return this / CycleProcesses.dayLengthMillis
}
fun Int.toMillisLong(): Long {
    return (this * CycleProcesses.dayLengthMillis).toLong()
}

object CycleProcesses {
    const val dayLengthMillis = 24 * 60 * 60 * 1000.0 // cast as Long for convenience
    const val lutealPhaseLength = 14 // in days


    fun median(lengths: List<Long>): Long {
        if (lengths.isEmpty()) throw IllegalArgumentException("List cannot be empty")
        val sorted = lengths.sorted()
        val middle = sorted.size / 2
        return if (sorted.size % 2 == 0) {
            (sorted[middle - 1] + sorted[middle]) / 2
        } else {
            sorted[middle]
        }
    }

    fun mad(lengths: List<Long>, median: Long): Long {
        if (lengths.isEmpty()) throw IllegalArgumentException("List cannot be empty")
        val deviations = lengths.map { kotlin.math.abs(it - median) }
        return median(deviations) // reuse median function
    }


    fun validLengths(lengths: List<Long>): List<Long> { // TODO Properly expand validity
        return lengths
    }

    fun isLengthValid(length: Long): Boolean { // TODO Properly expand validity
        return true
    }
    fun predictedNextStartMs(recentStartMs: Long, median: Long): Long {
        return recentStartMs + median
    }
    fun predictedNextOvulationMs(predictedNextOvulationMs: Long): Long {
        return predictedNextOvulationMs - lutealPhaseLength.toMillisLong()
    }
}