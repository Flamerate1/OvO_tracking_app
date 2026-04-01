package com.f1forhelp.ovo

import android.content.Context
import androidx.room.*
import com.f1forhelp.ovo.data.BleedEvent
import com.f1forhelp.ovo.data.BleedEvent.Companion.db
import com.f1forhelp.ovo.data.BleedEventDao
import com.f1forhelp.ovo.data.Cycle
import com.f1forhelp.ovo.data.CycleDao

@Database(entities = [BleedEvent::class, Cycle::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bleedEventDao(): BleedEventDao
    abstract fun cycleDao(): CycleDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ovo_db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}