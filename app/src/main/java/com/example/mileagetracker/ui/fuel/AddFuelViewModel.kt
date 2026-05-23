package com.example.mileagetracker.ui.fuel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddFuelViewModel(
    private val fuelRepository: FuelRepository,
    private val vehicleRepository: VehicleRepository
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
                odometer = value,
                errorMessage = null
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

    fun updateAmountPaid(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                amountPaid = value
            )
        calculateLitres()
    }

    fun updateFuelPrice(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                fuelPrice = value
            )
        calculateLitres()
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
            || state.amountPaid.isBlank()
            || state.fuelPrice.isBlank()
        ) {
            return
        }

        viewModelScope.launch {

            val lastEntry =
                fuelRepository.getLatestEntry(
                    vehicleId
                )

            val currentOdometer =
                state.odometer.toDouble()

            if (
                lastEntry != null &&
                currentOdometer <= lastEntry.odometerKm
            ) {
                _uiState.value =
                    state.copy(
                        errorMessage =
                            "Odometer must be greater than ${lastEntry.odometerKm.toInt()} km"
                    )
                return@launch
            }
            _uiState.value =
                state.copy(
                    errorMessage = null
                )
            fuelRepository.addFuelEntry(
                FuelEntry(
                    vehicleId = vehicleId,
                    dateMillis =
                        System.currentTimeMillis(),
                    odometerKm =
                        state.odometer.toDouble(),
                    amountPaid =
                        state.amountPaid.toDouble(),
                    fuelPrice =
                        state.fuelPrice.toDouble(),
                    litres =
                        state.litres.toDouble(),
                    fullTank =
                        state.fullTank
                )
            )

            vehicleRepository.updateFuelDefaults(
                vehicleId,
                state.amountPaid.toDouble(),
                state.fuelPrice.toDouble()
            )
        }
    }

    fun loadVehicleDefaults(
        vehicleId: Long
    ) {

        viewModelScope.launch {

            val vehicle =
                vehicleRepository
                    .getVehicleById(vehicleId)

            vehicle?.let {

                _uiState.value =
                    _uiState.value.copy(
                        amountPaid =
                            if (it.lastAmountPaid > 0)
                                it.lastAmountPaid.toInt().toString()
                            else "",

                        fuelPrice =
                            if (
                                it.lastFuelPrice > 0
                            )
                                it.lastFuelPrice.toString()
                            else ""
                    )

                calculateLitres()
            }
        }
    }
    @SuppressLint("DefaultLocale")
    private fun calculateLitres() {

        val amount = _uiState.value.amountPaid.toDoubleOrNull()

        val price = _uiState.value.fuelPrice.toDoubleOrNull()

        if (
            amount == null
            || price == null
            || price <= 0
        ) {
            _uiState.value =
                _uiState.value.copy(
                    litres = ""
                )
            return
        }

        val litres =
            amount / price

        _uiState.value =
            _uiState.value.copy(
                litres =
                    String.format(
                        "%.2f",
                        litres
                    )
            )
    }
}