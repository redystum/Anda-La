package com.redystum.andala.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.PousadaNights

class TripViewModel : ViewModel() {
    val maxNights = 6

    private val _selectedPousadas = mutableStateListOf<Pousada>()

    val selectedPousadas: List<Pousada> = _selectedPousadas

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
        Log.d("TripViewModel", "Setting nights for ${pousada.name}: $nights")
        if (_pousadaNights.any { it.pousada == pousada }) {
            Log.d("TripViewModel", "Pousada already exists in the list. Updating nights.")
            val index = _pousadaNights.indexOfFirst { it.pousada == pousada }
            val found = _pousadaNights[index]

            if (sumWithout(pousada.id) + nights > maxNights) {
                Log.d("TripViewModel", "Cannot set nights for ${pousada.name}: exceeds max nights")
                Log.d("TripViewModel", "Current sum without ${pousada.name}: ${sumWithout(pousada.id)}")
                return found.nights
            }
            _pousadaNights[index] = found.copy(nights = nights)
            Log.d("TripViewModel", "Nights updated for ${pousada.name}: $nights")
        } else {
            if (nights > maxNights) {
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

}