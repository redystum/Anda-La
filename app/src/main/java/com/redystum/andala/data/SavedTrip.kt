package com.redystum.andala.data

import com.redystum.andala.data.remote.RouteOption

/**
 * Everything that is persisted when the user confirms a trip.
 */
data class SavedTripInput(
    val originName: String,
    val originCoordinates: Pair<Double, Double>,
    val stops: List<SavedTripStop>,
    val routes: List<RouteOption>
)

data class SavedTripStop(
    val pousada: Pousada,
    val nights: Int,
    val order: Int
)

data class SavedTripSummary(
    val id: Long,
    val createdAt: Long,
    val originName: String,
    val totalNights: Int,
    val totalDurationMinutes: Int,
    val pousadaNames: List<String>
)

data class SavedTripDetails(
    val id: Long,
    val createdAt: Long,
    val originName: String,
    val originCoordinates: Pair<Double, Double>,
    val totalNights: Int,
    val totalDurationMinutes: Int,
    val stops: List<SavedTripStop>,
    val routes: List<RouteOption>
)
