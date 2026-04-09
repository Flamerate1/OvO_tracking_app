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

    @Query("DELETE FROM cycles WHERE predictionDateMs = :predictionDateMs")
    fun deleteByPredictionDateMs(predictionDateMs: Long)

    @Query("SELECT * FROM cycles ORDER BY startMs DESC")
    fun getAll(): List<Cycle>
}

@Dao
interface AnalysisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(analysis: Analysis)

    @Query("DELETE FROM analyses WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM analyses")
    fun deleteAll()

    @Query("SELECT * FROM analyses ORDER BY id DESC")
    fun getAll(): List<Analysis>
}