package com.redystum.andala.data.local

import com.redystum.andala.data.Pousada
import com.redystum.andala.data.SavedTripDetails
import com.redystum.andala.data.SavedTripInput
import com.redystum.andala.data.SavedTripStop
import com.redystum.andala.data.SavedTripSummary
import com.redystum.andala.data.remote.RouteOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SavedTripRepository(
    private val dao: SavedTripDao
) {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val savedTrips: Flow<List<SavedTripSummary>> =
        dao.observeAll().map { trips -> trips.map { it.toSummary() } }

    suspend fun saveTrip(input: SavedTripInput): Long {
        val stops = input.stops
            .sortedBy { it.order }
            .map { stop ->
                SavedTripStopEntity(
                    tripId = 0,
                    pousadaId = stop.pousada.id,
                    externalId = stop.pousada.externalId,
                    name = stop.pousada.name,
                    location = stop.pousada.location,
                    latitude = stop.pousada.coordinates.first,
                    longitude = stop.pousada.coordinates.second,
                    imageUrl = stop.pousada.imageUrl,
                    nights = stop.nights,
                    stopOrder = stop.order
                )
            }

        val routes = input.routes.map { route ->
            SavedTripRouteEntity(
                tripId = 0,
                legIndex = route.legIndex,
                routeJson = json.encodeToString(RouteOption.serializer(), route)
            )
        }

        val trip = SavedTripEntity(
            createdAt = System.currentTimeMillis(),
            originName = input.originName,
            originLatitude = input.originCoordinates.first,
            originLongitude = input.originCoordinates.second,
            totalNights = input.stops.sumOf { it.nights },
            totalDurationMinutes = input.routes.sumOf { it.durationMinutes ?: 0 }
        )

        return dao.saveTrip(trip, stops, routes)
    }

    suspend fun getTrip(id: Long): SavedTripDetails? =
        dao.getById(id)?.toDetails()

    suspend fun deleteTrip(id: Long) {
        dao.deleteById(id)
    }

    private fun SavedTripWithDetails.toSummary(): SavedTripSummary =
        SavedTripSummary(
            id = trip.id,
            createdAt = trip.createdAt,
            originName = trip.originName,
            totalNights = trip.totalNights,
            totalDurationMinutes = trip.totalDurationMinutes,
            pousadaNames = stops.sortedBy { it.stopOrder }.map { it.name }
        )

    private fun SavedTripWithDetails.toDetails(): SavedTripDetails =
        SavedTripDetails(
            id = trip.id,
            createdAt = trip.createdAt,
            originName = trip.originName,
            originCoordinates = trip.originLatitude to trip.originLongitude,
            totalNights = trip.totalNights,
            totalDurationMinutes = trip.totalDurationMinutes,
            stops = stops
                .sortedBy { it.stopOrder }
                .map { stop ->
                    SavedTripStop(
                        pousada = Pousada(
                            id = stop.pousadaId,
                            externalId = stop.externalId,
                            name = stop.name,
                            location = stop.location,
                            coordinates = stop.latitude to stop.longitude,
                            imageUrl = stop.imageUrl
                        ),
                        nights = stop.nights,
                        order = stop.stopOrder
                    )
                },
            routes = routes
                .sortedBy { it.legIndex }
                .mapNotNull { route ->
                    runCatching {
                        json.decodeFromString(RouteOption.serializer(), route.routeJson)
                    }.getOrNull()
                }
        )
}
