package com.redystum.andala.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class RoutesRequestDto(
    val origin: WaypointDto,
    val destination: WaypointDto,
    val travelMode: String = "TRANSIT",
    val computeAlternativeRoutes: Boolean = true,
    val languageCode: String = "pt-PT",
    val units: String = "METRIC",
    val departureTime: String? = null
)

@Serializable
data class WaypointDto(
    val location: LocationDto
)

@Serializable
data class LocationDto(
    val latLng: LatLngDto
)

@Serializable
data class LatLngDto(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class RoutesResponseDto(
    val routes: List<RouteDto> = emptyList()
)

@Serializable
data class RouteDto(
    val duration: String? = null,
    val legs: List<LegDto> = emptyList()
)

data class RouteOption(
    val route: RouteDto,
    val departureTime: String,
    val legIndex: Int,
    val origin: LatLngDto,
    val destination: LatLngDto,
    val path: List<LatLngDto>
) {
    val durationMinutes: Int?
        get() = route.duration
            ?.removeSuffix("s")
            ?.toIntOrNull()
            ?.div(60)

    val transitLines: List<String>
        get() = route.legs
            .flatMap { it.steps }
            .mapNotNull { it.transitDetails?.transitLine?.nameShort }
            .distinct()
}

@Serializable
data class LegDto(
    val steps: List<StepDto> = emptyList()
)

@Serializable
data class StepDto(
    val travelMode: String? = null,
    val transitDetails: TransitDetailsDto? = null
)

@Serializable
data class TransitDetailsDto(
    val stopDetails: StopDetailsDto? = null,
    val localizedValues: LocalizedValuesDto? = null,
    val headsign: String? = null,
    val transitLine: TransitLineDto? = null,
    val stopCount: Int? = null
)

@Serializable
data class StopDetailsDto(
    val arrivalStop: TransitStopDto? = null,
    val arrivalTime: String? = null,
    val departureStop: TransitStopDto? = null,
    val departureTime: String? = null
)

@Serializable
data class TransitStopDto(
    val name: String? = null,
    val location: LocationDto? = null
)

@Serializable
data class LocalizedValuesDto(
    val arrivalTime: LocalizedTimeDto? = null,
    val departureTime: LocalizedTimeDto? = null
)

@Serializable
data class LocalizedTimeDto(
    val time: TimeTextDto? = null,
    val timeZone: String? = null
)

@Serializable
data class TimeTextDto(
    val text: String? = null
)

@Serializable
data class TransitLineDto(
    val agencies: List<TransitAgencyDto> = emptyList(),
    val name: String? = null,
    val color: String? = null,
    val nameShort: String? = null,
    val textColor: String? = null,
    val vehicle: VehicleDto? = null
)

@Serializable
data class TransitAgencyDto(
    val name: String? = null,
    val phoneNumber: String? = null,
    val uri: String? = null
)

@Serializable
data class VehicleDto(
    val name: TimeTextDto? = null,
    val type: String? = null,
    val iconUri: String? = null,
    val localIconUri: String? = null
)
