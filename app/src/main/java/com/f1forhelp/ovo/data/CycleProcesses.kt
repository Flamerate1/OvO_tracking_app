package com.f1forhelp.ovo.data

import android.util.Log
import kotlin.math.exp
import kotlin.math.abs
import kotlin.math.ln

fun Long.toDaysDouble(): Double {
    return this / CycleProcesses.DAY_LENGTH_MILLIS
}
fun Int.toMillisLong(): Long {
    return (this * CycleProcesses.DAY_LENGTH_MILLIS).toLong()
}

data class Estimate(
    val center: Long,
    val margin: Long
)

object CycleProcesses {
    const val DAY_LENGTH_MILLIS = 24 * 60 * 60 * 1000.0 // cast as Long for convenience
    const val LUTEAL_PHASE_LENGTH = 14 // in days

    fun computeEstimate(lengths: List<Long>): Estimate {
        require(lengths.isNotEmpty()) { "Cycle list cannot be empty" }
        Log.d("calculations","Doing a NON-weighted computation!")

        // Center: median
        val sorted = lengths.sorted()
        val middle = sorted.size / 2
        val center = if (lengths.size % 2 == 0) {
            (sorted[middle - 1] + sorted[middle]) / 2
        } else {
            sorted[middle]
        }

        // Margin: median absolute deviation
        val deviations = sorted.map { abs(it - center) }.sorted()
        val margin = if (deviations.size % 2 == 0) {
            (deviations[deviations.size / 2 - 1] + deviations[deviations.size / 2]) / 2
        } else {
            deviations[deviations.size / 2]
        }

        Log.d("calculations","$margin")
        return Estimate(center, margin)
    }

    fun computeWeightedEstimate(lengths: List<Long>, halfLife: Double = 10.0): Estimate {
        require(lengths.isNotEmpty()) { "Cycle list cannot be empty" }
        Log.d("calculations","Doing a WEIGHTED computation!")

        val lambda = ln(2.0) / halfLife
        val n = lengths.size

        // Compute weights: most recent cycle
        val weights = lengths.indices.map { i ->
            val age = n - 1 - i  // 0 = most recent
            exp(-lambda * age)
        }

        val sumWeights = weights.sum()

        // Weighted mean = center
        val centerDouble = lengths.zip(weights).sumOf { (x, w) -> x * w } / sumWeights
        val center = centerDouble.toLong()

        // Weighted MAD = margin
        val deviations = lengths.zip(weights).map { (x, w) -> Pair(abs(x - centerDouble), w) }
            .sortedBy { it.first }

        var cumulativeWeight = 0.0
        val halfTotalWeight = sumWeights / 2.0
        var margin = deviations.last().first
        for ((dev, w) in deviations) {
            cumulativeWeight += w
            if (cumulativeWeight >= halfTotalWeight) {
                margin = dev
                break
            }
        }

        Log.d("calculations","$margin")
        return Estimate(center, margin.toLong())
    }

    fun sanityFilter(lengths: List<Long>, minDays: Long = 15, maxDays: Long = 60): List<Long> {
        return lengths.filter { it in minDays..maxDays }
    }

    // Outlier filter: iterative removal based on median ± k * MAD
    fun outlierFilter(lengths: List<Long>, k: Double = 2.0): List<Long> {
        if (lengths.isEmpty()) return lengths.toList()

        var filtered = lengths.toMutableList()
        var changed: Boolean

        do {
            changed = false
            val estimate = computeEstimate(filtered) // USE UN-WEIGHTED!!!
            val lower = estimate.center - (k * estimate.margin).toLong()
            val upper = estimate.center + (k * estimate.margin).toLong()

            val beforeSize = filtered.size
            filtered = filtered.filter { it in lower..upper }.toMutableList()
            if (filtered.size < beforeSize) changed = true
        } while (changed && filtered.isNotEmpty())

        return filtered.toList()
    }


    fun validLengths(lengths: List<Long>): List<Long> { // TODO Properly expand validity
        return lengths
    }

    fun isLengthValid(length: Long, minDays: Long = 15, maxDays: Long = 45): Boolean {
        return length.toDaysDouble().toLong() in minDays..maxDays
    }
    fun predictedNextStartMs(recentStartMs: Long, median: Long): Long {
        return recentStartMs + median
    }
    fun predictedNextOvulationMs(predictedNextOvulationMs: Long): Long {
        return predictedNextOvulationMs - LUTEAL_PHASE_LENGTH.toMillisLong()
    }




}