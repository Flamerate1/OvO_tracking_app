package com.f1forhelp.ovo.data

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.f1forhelp.ovo.AppDatabase
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = "bleed_events")
data class BleedEvent(
    @PrimaryKey val epochMillis: Long
) {
    companion object {
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
        fun delete(epochMillis : Long) { db!!.bleedEventDao().deleteByEpoch(epochMillis)}

        // Observers
        private val observers = mutableListOf<() -> Unit>()
        fun notifyObservers() { observers.forEach { it.invoke() } }
        fun addObserver(observer: () -> Unit) { observers.add(observer) }
        fun removeObserver(observer: () -> Unit) { observers.remove(observer) }

    }
    fun save() {
        db!!.bleedEventDao().insert(this)
        BleedEvent.notifyObservers()
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
    @PrimaryKey val start_ms: Long,
    val next_start_ms: Long?,
    val length_days: Int?,
    val valid: Boolean,
    val median_cycle_len_days: Double?,
    val mad_cycle_len_days: Double?,
    val valid_cycle_count: Int?,
    val predicted_next_start_ms: Long?,
    val predicted_next_ovulation_ms: Long?
)