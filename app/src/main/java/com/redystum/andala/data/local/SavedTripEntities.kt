package com.redystum.andala.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "saved_trips")
data class SavedTripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long,
    val originName: String,
    val originLatitude: Double,
    val originLongitude: Double,
    val totalNights: Int,
    val totalDurationMinutes: Int
)

@Entity(
    tableName = "saved_trip_stops",
    primaryKeys = ["tripId", "pousadaId"],
    foreignKeys = [
        ForeignKey(
            entity = SavedTripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class SavedTripStopEntity(
    val tripId: Long,
    val pousadaId: Int,
    val externalId: Int,
    val name: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String?,
    val nights: Int,
    val stopOrder: Int
)

@Entity(
    tableName = "saved_trip_routes",
    primaryKeys = ["tripId", "legIndex"],
    foreignKeys = [
        ForeignKey(
            entity = SavedTripEntity::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("tripId")]
)
data class SavedTripRouteEntity(
    val tripId: Long,
    val legIndex: Int,
    val routeJson: String
)

data class SavedTripWithDetails(
    @Embedded val trip: SavedTripEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val stops: List<SavedTripStopEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "tripId"
    )
    val routes: List<SavedTripRouteEntity>
)
