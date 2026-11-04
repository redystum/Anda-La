package com.redystum.andala.data.remote

import android.util.Log
import com.redystum.andala.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RoutesApi(
    private val client: HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }
    }
) {
    private val endpoint = "https://routes.googleapis.com/directions/v2:computeRoutes"
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    suspend fun computeRoutes(request: RoutesRequestDto): RoutesResponseDto {
        Log.d(
            "RoutesApi",
            "Requesting route: " +
                "(${request.origin.location.latLng.latitude}, " +
                "${request.origin.location.latLng.longitude}) -> " +
                "(${request.destination.location.latLng.latitude}, " +
                "${request.destination.location.latLng.longitude}), " +
                "departure=${request.departureTime}"
        )
        Log.d("RoutesApi", "Request body: ${json.encodeToString(request)}")

        val response = client.post(endpoint) {
            contentType(ContentType.Application.Json)
            header("X-Goog-Api-Key", BuildConfig.MAPS_API_KEY)
            header(
                "X-Goog-FieldMask",
                "routes.duration,routes.legs.steps.travelMode," +
                    "routes.legs.steps.transitDetails"
            )
            setBody(request)
        }
        val responseBody = response.bodyAsText()

        Log.d("RoutesApi", "HTTP ${response.status.value} from Routes API")
        Log.d("RoutesApi", "Routes API response: $responseBody")

        return json.decodeFromString<RoutesResponseDto>(responseBody)
    }
}
