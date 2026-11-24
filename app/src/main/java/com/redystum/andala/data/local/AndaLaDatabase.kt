package com.redystum.andala.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedTripEntity::class,
        SavedTripStopEntity::class,
        SavedTripRouteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AndaLaDatabase : RoomDatabase() {

    abstract fun savedTripDao(): SavedTripDao

    companion object {
        @Volatile
        private var instance: AndaLaDatabase? = null

        fun getInstance(context: Context): AndaLaDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AndaLaDatabase::class.java,
                    "andala.db"
                ).build().also { instance = it }
            }
    }
}
