package com.f1forhelp.ovo.data

import androidx.room.*

@Dao
interface BleedEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event: BleedEvent)

    @Query("DELETE FROM bleed_events WHERE epochMillis = :epochMillis")
    fun deleteByEpoch(epochMillis: Long)

    @Query("SELECT * FROM bleed_events ORDER BY epochMillis DESC")
    fun getAll(): List<BleedEvent>
}

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cycle: Cycle)

    @Query("SELECT * FROM cycles ORDER BY start_ms DESC")
    fun getAll(): List<Cycle>
}