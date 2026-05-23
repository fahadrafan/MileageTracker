package com.example.mileagetracker.ui.fuel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.data.repository.FuelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddFuelViewModel(
    private val repository: FuelRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AddFuelUiState())

    val uiState: StateFlow<AddFuelUiState> =
        _uiState.asStateFlow()

    fun updateOdometer(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                odometer = value
            )
    }

    fun updateLitres(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                litres = value
            )
    }

    fun updateCost(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                cost = value
            )
    }

    fun updateFullTank(
        value: Boolean
    ) {
        _uiState.value =
            _uiState.value.copy(
                fullTank = value
            )
    }

    fun saveFuel(
        vehicleId: Long
    ) {

        val state = _uiState.value

        if (
            state.odometer.isBlank()
            || state.litres.isBlank()
            || state.cost.isBlank()
        ) {
            return
        }

        viewModelScope.launch {

            repository.addFuelEntry(
                FuelEntry(
                    vehicleId = vehicleId,
                    dateMillis =
                        System.currentTimeMillis(),
                    odometerKm =
                        state.odometer.toDouble(),
                    litres =
                        state.litres.toDouble(),
                    cost =
                        state.cost.toDouble(),
                    fullTank =
                        state.fullTank
                )
            )
        }
    }
}