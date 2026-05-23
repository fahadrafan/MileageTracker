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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddFuelViewModel(
    private val fuelRepository: FuelRepository,
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddFuelUiState())

    val uiState: StateFlow<AddFuelUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(refillDateText = todayDateText())
    }

    private fun todayDateText(): String {
        return SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(Date())
    }

    private fun parseDateMillis(dateText: String): Long {
        return try {
            val formatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
            formatter.isLenient = false
            formatter.parse(dateText)?.time ?: System.currentTimeMillis()
        } catch (_: Exception) {
            System.currentTimeMillis()
        }
    }

    private fun isFutureDate(dateText: String): Boolean {

        return try {
            val formatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
            formatter.isLenient = false
            val date = formatter.parse(dateText) ?: return true
            date.after(Date())
        } catch (_: Exception) {
            true
        }
    }

    fun updateDateText(value: String) {
        _uiState.value = _uiState.value.copy(refillDateText = value)
    }

    fun onDateFocusLost() {
        formatDateIfNeeded()
    }

    private fun formatDateIfNeeded() {
        val text = _uiState.value.refillDateText
        if (text.length != 8 || !text.all { it.isDigit() }) {
            return
        }

        try {
            val inputFormat = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
            inputFormat.isLenient = false
            val outputFormat = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
            val date = inputFormat.parse(text) ?: return

            if (date.after(Date())) {
                _uiState.value = _uiState.value.copy(errorMessage = "Future dates are not allowed")
                return
            }

            _uiState.value =
                _uiState.value.copy(refillDateText = outputFormat.format(date), errorMessage = null)

        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid date")
        }
    }

    fun updateOdometer(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(odometer = value, errorMessage = null)
    }

    fun updateLitres(value: String) {
        _uiState.value = _uiState.value.copy(litres = value)
    }

    fun updateAmountPaid(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(
            amountPaid = value,
            errorMessage = null
        )
        calculateLitres()
    }

    fun updateFuelPrice(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(
            fuelPrice = value,
            errorMessage = null
        )
        calculateLitres()
    }

    fun updateFullTank(value: Boolean) {
        _uiState.value = _uiState.value.copy(fullTank = value)
    }

    fun saveFuel(vehicleId: Long) {
        val state = _uiState.value

        if (isFutureDate(state.refillDateText)) {
            _uiState.value = state.copy(errorMessage = "Future dates are not allowed")
            return
        }

        if (state.odometer.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter odometer reading")
            return
        }

        if (state.amountPaid.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter amount paid")
            return
        }

        if (state.fuelPrice.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter fuel price")
            return
        }

        viewModelScope.launch {

            val lastEntry = fuelRepository.getLatestEntry(vehicleId)

            val currentOdometer = state.odometer.toDouble()

            if (
                lastEntry != null &&
                currentOdometer <= lastEntry.odometerKm
            ) {
                _uiState.value =
                    state.copy(errorMessage = "Current odometer must be greater than the previous entry ${lastEntry.odometerKm.toInt()} km")
                return@launch
            }
            _uiState.value = state.copy(errorMessage = null)

            fuelRepository.addFuelEntry(
                FuelEntry(
                    vehicleId = vehicleId,
                    dateMillis = parseDateMillis(state.refillDateText),
                    odometerKm = state.odometer.toDouble(),
                    amountPaid = state.amountPaid.toDouble(),
                    fuelPrice = state.fuelPrice.toDouble(),
                    litres = state.litres.toDouble(),
                    fullTank = state.fullTank
                )
            )

            vehicleRepository.updateFuelDefaults(
                vehicleId,
                state.amountPaid.toDouble(),
                state.fuelPrice.toDouble()
            )
            _uiState.value = _uiState.value.copy(saveSuccessful = true)
        }
    }

    fun loadVehicleDefaults(vehicleId: Long) {

        viewModelScope.launch {

            val vehicle = vehicleRepository.getVehicleById(vehicleId)

            val lastEntry = fuelRepository.getLatestEntry(vehicleId)
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
                            else "",

                        lastOdometer =
                            lastEntry?.odometerKm
                                ?.toInt()
                                ?.toString()
                                ?: "",
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

        val litres = amount / price

        _uiState.value = _uiState.value.copy(litres = String.format("%.2f", litres))
    }
}