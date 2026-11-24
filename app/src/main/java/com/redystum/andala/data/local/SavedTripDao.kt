package com.redystum.andala.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SavedTripDao {

    @Transaction
    @Query("SELECT * FROM saved_trips ORDER BY createdAt DESC")
    abstract fun observeAll(): Flow<List<SavedTripWithDetails>>

    @Transaction
    @Query("SELECT * FROM saved_trips WHERE id = :id")
    abstract suspend fun getById(id: Long): SavedTripWithDetails?

    @Insert
    abstract suspend fun insertTrip(trip: SavedTripEntity): Long

    @Insert
    abstract suspend fun insertStops(stops: List<SavedTripStopEntity>)

    @Insert
    abstract suspend fun insertRoutes(routes: List<SavedTripRouteEntity>)

    @Query("DELETE FROM saved_trips WHERE id = :id")
    abstract suspend fun deleteById(id: Long)

    @Transaction
    open suspend fun saveTrip(
        trip: SavedTripEntity,
        stops: List<SavedTripStopEntity>,
        routes: List<SavedTripRouteEntity>
    ): Long {
        val tripId = insertTrip(trip)
        insertStops(stops.map { it.copy(tripId = tripId) })
        insertRoutes(routes.map { it.copy(tripId = tripId) })
        return tripId
    }
}
