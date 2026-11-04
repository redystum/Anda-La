package com.redystum.andala.data.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class RoutesService(
    private val api: RoutesApi = RoutesApi()
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getRoutesForTrip(stops: List<LatLngDto>): List<RouteOption> {
        if (stops.size < 2) {
            Log.w("RoutesService", "Not enough stops to request routes: ${stops.size}")
            return emptyList()
        }

        val departureTimes = listOf(7, 9, 11, 13).map { hour ->
            ZonedDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(hour, 0),
                ZoneId.systemDefault()
            ).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }

        Log.d(
            "RoutesService",
            "Loading routes for ${stops.size - 1} trip leg(s) at " +
                "${departureTimes.joinToString()}"
        )

        val routes = stops.zipWithNext().flatMapIndexed { legIndex, (origin, destination) ->
            departureTimes.flatMap { departureTime ->
                try {
                    val response = api.computeRoutes(
                        RoutesRequestDto(
                            origin = WaypointDto(LocationDto(origin)),
                            destination = WaypointDto(LocationDto(destination)),
                            departureTime = departureTime
                        )
                    )
                    Log.d(
                        "RoutesService",
                        "Leg ${legIndex + 1}/${stops.size - 1}, " +
                            "departure=$departureTime returned " +
                            "${response.routes.size} route(s)"
                    )
                    response.routes.map { route ->
                        RouteOption(
                            route = route,
                            departureTime = departureTime,
                            legIndex = legIndex,
                            origin = origin,
                            destination = destination,
                            path = routePath(route, origin, destination)
                        )
                    }
                } catch (exception: Exception) {
                    Log.e(
                        "RoutesService",
                        "Unable to compute route for leg $legIndex at $departureTime",
                        exception
                    )
                    emptyList()
                }
            }
        }

        Log.d("RoutesService", "Finished route loading: ${routes.size} option(s)")
        return routes
    }

    private fun routePath(
        route: RouteDto,
        origin: LatLngDto,
        destination: LatLngDto
    ): List<LatLngDto> {
        val transitStops = route.legs
            .flatMap { it.steps }
            .mapNotNull { it.transitDetails?.stopDetails }
            .flatMap { details ->
                listOfNotNull(
                    details.departureStop?.location?.latLng,
                    details.arrivalStop?.location?.latLng
                )
            }
            .distinct()

        return listOf(origin) + transitStops + destination
    }
}
