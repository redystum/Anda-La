package com.redystum.andala.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.Snapshot
import androidx.lifecycle.ViewModel
import com.redystum.andala.data.Pousada
import com.redystum.andala.data.PousadaNights

class TripViewModel : ViewModel() {
    val maxNights = 6
    val minNights = 2

    private val _selectedPousadas = mutableStateListOf<Pousada>()

    val selectedPousadas: List<Pousada> = _selectedPousadas

    // Leiria by default just to save Google api requests
    private val _selectedLocation = mutableStateOf<String?>("Leiria")
    val selectedLocation: String?
        get() = _selectedLocation.value

    fun setSelectedLocation(location: String) {
        _selectedLocation.value = location
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

}