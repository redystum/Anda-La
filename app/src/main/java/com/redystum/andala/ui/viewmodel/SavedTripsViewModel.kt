package com.redystum.andala.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.redystum.andala.data.SavedTripDetails
import com.redystum.andala.data.SavedTripInput
import com.redystum.andala.data.SavedTripSummary
import com.redystum.andala.data.local.AndaLaDatabase
import com.redystum.andala.data.local.SavedTripRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SavedTripsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SavedTripRepository(
        AndaLaDatabase.getInstance(application).savedTripDao()
    )

    val savedTrips: StateFlow<List<SavedTripSummary>?> = repository.savedTrips
        .map { trips -> trips as List<SavedTripSummary>? }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    suspend fun saveTrip(input: SavedTripInput): Long? =
        runCatching { repository.saveTrip(input) }
            .onFailure { Log.e("SavedTripsViewModel", "Unable to save trip", it) }
            .getOrNull()

    suspend fun getTrip(id: Long): SavedTripDetails? =
        runCatching { repository.getTrip(id) }
            .onFailure { Log.e("SavedTripsViewModel", "Unable to load trip $id", it) }
            .getOrNull()

    suspend fun deleteTrip(id: Long) {
        runCatching { repository.deleteTrip(id) }
            .onFailure { Log.e("SavedTripsViewModel", "Unable to delete trip $id", it) }
    }
}
