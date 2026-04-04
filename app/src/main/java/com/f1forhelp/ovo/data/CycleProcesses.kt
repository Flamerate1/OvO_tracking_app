package com.f1forhelp.ovo.data

import kotlin.math.exp
import kotlin.math.abs
import kotlin.math.ln

fun Long.toDaysDouble(): Double {
    return this / CycleProcesses.dayLengthMillis
}
fun Int.toMillisLong(): Long {
    return (this * CycleProcesses.dayLengthMillis).toLong()
}

data class Estimate(
    val center: Long,
    val margin: Long
)

object CycleProcesses {
    const val dayLengthMillis = 24 * 60 * 60 * 1000.0 // cast as Long for convenience
    const val lutealPhaseLength = 14 // in days

    object settings {
        var exclusionDeviationCap = 2 // how many standard deviations a value can be before capping amount of cycles.
        var inclusionCap = 24 // How many recent cycle lengths are included

        // Weighted Settings
        var useWeighted = true // do or don't use weighted calculation
        var weightedInclusionCap = 48 // cap on included data before weighting
        var halfLife = 10.0 // the half-life on the importance of a cycle length as it goes from recent to old

    }

    fun computeEstimate(lengths: List<Long>): Estimate {
        require(lengths.isNotEmpty()) { "Cycle list cannot be empty" }

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

        return Estimate(center, margin)
    }

    fun computeWeightedEstimate(lengths: List<Long>, halfLife: Double = 10.0): Estimate {
        require(lengths.isNotEmpty()) { "Cycle list cannot be empty" }

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

        return Estimate(center, margin.toLong())
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