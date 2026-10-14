package com.redystum.andala.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.redystum.andala.data.Pousada

class TripViewModel : ViewModel() {
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

}