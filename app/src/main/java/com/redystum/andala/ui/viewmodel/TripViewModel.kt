package com.redystum.andala.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.PousadaNights
import com.redystum.andala.data.remote.LatLngDto
import com.redystum.andala.data.remote.RouteOption
import com.redystum.andala.data.remote.RoutesService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TripViewModel : ViewModel() {
    val maxNights = 6
    val minNights = 2

    private val routesService = RoutesService()
    private var routesJob: Job? = null
    private var routesKey: String? = null

    var routeOptions by mutableStateOf<List<RouteOption>>(emptyList())
        private set

    var selectedRouteByLeg by mutableStateOf<Map<Int, Int>>(emptyMap())
        private set

    var isLoadingRoutes by mutableStateOf(false)
        private set

    private val _selectedPousadas = mutableStateListOf<Pousada>()

    val selectedPousadas: List<Pousada> = _selectedPousadas

    // Leiria by default just to save Google api requests
    private val _selectedLocation = mutableStateOf<String?>("Leiria")
    val selectedLocation: String?
        get() = _selectedLocation.value

    private val _selectedLocationCoordinates = mutableStateOf<LatLng?>(LatLng(39.74953310000001, -8.807682999999999))
    val selectedLocationCoordinates: LatLng?
        get() = _selectedLocationCoordinates.value

    fun setSelectedLocation(location: String, coordinates: LatLng) {
        _selectedLocation.value = location
        _selectedLocationCoordinates.value = coordinates
        Log.d("TripViewModel", "Selected location: $location")
        Log.d("TripViewModel", "Selected coordinates: $coordinates")
    }

    /**
     * Toggles a pousada selection.
     * Returns true if successful, or false if the max limit (3) was reached.
     */
    fun toggleSelection(pousada: Pousada): Boolean {
        return if (_selectedPousadas.contains(pousada)) {
            _selectedPousadas.remove(pousada)
            true
        } else {
            if (_selectedPousadas.size < 3) {
                _selectedPousadas.add(pousada)
                true
            } else {
                false // Max limit of 3 reached
            }
        }
    }

    private val _pousadaNights = mutableStateListOf<PousadaNights>()
    val pousadaNights: List<PousadaNights> = _pousadaNights

    private fun sumWithout(id: Int): Int {
        return _pousadaNights.filter { it.pousada.id != id }.sumOf { it.nights }
    }

    fun setPousadaNights(pousada: Pousada, nights: Int): Int {
        if (_pousadaNights.any { it.pousada == pousada }) {
            val index = _pousadaNights.indexOfFirst { it.pousada == pousada }
            val found = _pousadaNights[index]

            if (nights < minNights) {
                return found.nights
            }

            if (sumWithout(pousada.id) + nights > maxNights) {
                return found.nights
            }
            _pousadaNights[index] = found.copy(nights = nights)
        } else {
            if (getSumNights() + nights > maxNights) {
                return 0
            }
            if (nights < minNights) {
                return 0
            }
            _pousadaNights.add(PousadaNights(pousada, nights))
        }
        return nights
    }

    fun getSumNights(): Int {
        return _pousadaNights.sumOf { it.nights }
    }

    fun reorderPousada(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in _pousadaNights.indices || toIndex !in _pousadaNights.indices) {
            return
        }

        val reordered = _pousadaNights.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }
        Snapshot.withMutableSnapshot {
            reordered.forEachIndexed { index, stop ->
                _pousadaNights[index] = stop.copy(order = index)
            }
        }
    }

    fun convertPousadaToNights() {
        val nights: Int = maxNights / _selectedPousadas.size
        _pousadaNights.clear()
        _selectedPousadas.forEachIndexed { index, pousada ->
            _pousadaNights.add(PousadaNights(pousada, nights, index))
        }
    }

    /**
     * Loads the route options once per trip configuration. Keeping this in the
     * ViewModel means a configuration change (e.g. screen rotation) reuses the
     * already loaded routes instead of requesting them from the API again.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadRoutes(origin: LatLng?, pousadas: List<Pousada>) {
        if (origin == null || pousadas.isEmpty()) return

        val key = buildString {
            append(origin.latitude).append(',').append(origin.longitude)
            pousadas.forEach { pousada ->
                append('|').append(pousada.id).append(':')
                    .append(pousada.coordinates.first).append(',')
                    .append(pousada.coordinates.second)
            }
        }
        if (key == routesKey) return

        routesKey = key
        routesJob?.cancel()
        routesJob = viewModelScope.launch {
            isLoadingRoutes = true
            routeOptions = emptyList()
            selectedRouteByLeg = emptyMap()

            try {
                val stops = buildList {
                    add(LatLngDto(origin.latitude, origin.longitude))
                    pousadas.forEach { pousada ->
                        add(
                            LatLngDto(
                                pousada.coordinates.first,
                                pousada.coordinates.second
                            )
                        )
                    }
                    add(LatLngDto(origin.latitude, origin.longitude))
                }

                val routes = routesService.getRoutesForTrip(stops)
                routeOptions = routes
                selectedRouteByLeg = routes
                    .groupBy { it.legIndex }
                    .mapValues { (_, options) -> options.indices.first() }
            } catch (exception: Exception) {
                Log.e("TripViewModel", "Unable to load routes", exception)
            } finally {
                if (routesKey == key) {
                    isLoadingRoutes = false
                    // Allow a retry if nothing could be loaded.
                    if (routeOptions.isEmpty()) routesKey = null
                }
            }
        }
    }

    fun selectRoute(legIndex: Int, optionIndex: Int) {
        selectedRouteByLeg = selectedRouteByLeg + (legIndex to optionIndex)
    }
}