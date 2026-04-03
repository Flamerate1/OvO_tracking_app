package com.f1forhelp.ovo.data

import androidx.room.*

@Dao
interface BleedEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(event: BleedEvent)

    @Query("DELETE FROM bleedEvents WHERE epochMillis = :epochMillis")
    fun deleteByEpoch(epochMillis: Long)

    @Query("SELECT * FROM bleedEvents ORDER BY epochMillis DESC")
    fun getAll(): List<BleedEvent>
}

@Dao
interface CycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cycle: Cycle)
    @Query("DELETE FROM cycles WHERE startMs = :startMs")
    fun deleteByEpoch(startMs: Long)

    @Query("SELECT * FROM cycles ORDER BY startMs DESC")
    fun getAll(): List<Cycle>
}