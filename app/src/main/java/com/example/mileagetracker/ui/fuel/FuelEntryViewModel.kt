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
import com.example.mileagetracker.ui.fuel.validation.ChronologyValidationResult
import com.example.mileagetracker.ui.fuel.validation.ValidationType

class FuelEntryViewModel(
    private val fuelRepository: FuelRepository,
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FuelEntryUiState())
    private var editingEntryId: Long? = null
    private var isEditMode = false
    private var bypassSoftWarning = false
    private var originalOdometerKm: Double? = null
    private var originalDateMillis: Long? = null

    val uiState: StateFlow<FuelEntryUiState> = _uiState.asStateFlow()

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
            val date = formatter.parse(dateText) ?: return false
            date.after(Date())
        } catch (_: Exception) {
            false
        }
    }

    fun updateDateText(value: String) {
        _uiState.value = _uiState.value.copy(
            refillDateText = value,
            dateError = null
        )
    }

    fun setDateMillis(dateMillis: Long) {
        val formatter = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())

        _uiState.value = _uiState.value.copy(
            refillDateText = formatter.format(
                Date(dateMillis)
            ),
            dateError = null
        )
    }

    fun onDateFocusLost() {
        formatDateIfNeeded()
    }

    fun onOdometerFocusLost() {
        val error = validateOdometer()
        if (error != null) {
            _uiState.value = _uiState.value.copy(odometerError = error)
        }
    }

    private fun formatDateIfNeeded() {

        val text = _uiState.value.refillDateText.trim()
        if (text.isBlank()) {
            return
        }
        // Already formatted date?
        try {
            val formatter =
                SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
                    .apply { isLenient = false }
            formatter.parse(text)
            if (isFutureDate(text)) {
                _uiState.value =
                    _uiState.value.copy(
                        dateError = "Future dates are not allowed"
                    )
            } else {
                _uiState.value =
                    _uiState.value.copy(
                        dateError = null
                    )
            }
            return
        } catch (_: Exception) {
        }

        if (!text.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(dateError = "Invalid date")
            return
        }

        val inputPattern =
            when (text.length) {
                6 -> "ddMMyy"
                8 -> "ddMMyyyy"
                else -> {
                    _uiState.value = _uiState.value.copy(dateError = "Invalid date")
                    return
                }
            }

        try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
                .apply {
                    isLenient = false
                }
            val outputFormat = SimpleDateFormat("dd-MMM-yy", Locale.getDefault())
            val date = inputFormat.parse(text) ?: return
            if (date.after(Date())) {

                _uiState.value =
                    _uiState.value.copy(
                        refillDateText =
                            outputFormat.format(date),
                        dateError = "Future dates are not allowed"
                    )

                return
            }
            _uiState.value =
                _uiState.value.copy(refillDateText = outputFormat.format(date), dateError = null)

        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(dateError = "Invalid date")
        }
    }

    private fun validateOdometer(): String? {
        val value = _uiState.value.odometer.toDoubleOrNull() ?: return null

        return if (value <= 0) "Odometer must be greater than 0"
        else null
    }

    fun updateOdometer(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(odometer = value, odometerError = null)
    }

    fun updateLitres(value: String) {
        _uiState.value = _uiState.value.copy(fuelQuantity = value)
    }

    fun updateAmountPaid(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(
            amountPaid = value,
            amountPaidError = null
        )
        calculateLitres()
    }

    fun updateFuelPrice(value: String) {
        if (value.contains("-")) {
            return
        }
        _uiState.value = _uiState.value.copy(
            fuelPrice = value,
            fuelPriceError = null
        )
        calculateLitres()
    }

    fun updateFullTank(value: Boolean) {
        _uiState.value = _uiState.value.copy(fullTank = value)
    }

    fun saveFuel(vehicleId: Long) {
        val state = _uiState.value

        var dateError: String? = null
        var odometerError: String? = null
        var amountPaidError: String? = null
        var fuelPriceError: String? = null

        val formatter =
            SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).apply {
                isLenient = false
            }

        try {
            formatter.parse(state.refillDateText)
        } catch (_: Exception) {
            dateError = "Invalid date"
        }

        if (state.refillDateText.isBlank()) {
            dateError = "Please enter a valid date"
        }

        if (dateError == null && isFutureDate(state.refillDateText)) {
            dateError = "Future dates are not allowed"
        }

        if (state.odometer.isBlank()) {
            odometerError = "Please enter odometer reading"
        } else {
            odometerError = validateOdometer()
        }

        if (state.amountPaid.isBlank()) {
            amountPaidError = "Please enter amount paid"
        }

        if (state.fuelPrice.isBlank()) {
            fuelPriceError = "Please enter fuel price"
        }

        _uiState.value = state.copy(
            dateError = dateError,
            odometerError = odometerError,
            amountPaidError = amountPaidError,
            fuelPriceError = fuelPriceError
        )

        if (
            dateError != null ||
            odometerError != null ||
            amountPaidError != null ||
            fuelPriceError != null
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                dateError = null,
                odometerError = null,
                amountPaidError = null,
                fuelPriceError = null,
                chronologyError = null
            )

            val chronologyResult =
                validateChronology(
                    vehicleId = vehicleId,
                    dateMillis = parseDateMillis(state.refillDateText),
                    odometerKm = state.odometer.toDouble()
                )

            if (chronologyResult.type == ValidationType.HARD_BLOCK) {
                println("HARD BLOCK: ${chronologyResult.conflictEntryNumber}")
                val dateText =
                    chronologyResult.conflictDateMillis
                        ?.let {
                            SimpleDateFormat(
                                "dd-MMM-yy",
                                Locale.getDefault()
                            ).format(Date(it))
                        } ?: ""
                _uiState.value =
                    _uiState.value.copy(
                        chronologyError =
                            """
                            This entry conflicts with:
                            
                            Entry #${chronologyResult.conflictEntryNumber}
                            $dateText
                            ${chronologyResult.conflictOdometerKm?.toInt()} km

                            Please edit or delete the conflicting entry first.
                            """.trimIndent()
                    )
                return@launch
            }
            if (
                chronologyResult.type == ValidationType.SOFT_WARNING &&
                !bypassSoftWarning
            ) {
                _uiState.value =
                    _uiState.value.copy(
                        showSoftWarningDialog = true
                    )

                return@launch
            }

            val fuelEntry = FuelEntry(
                id = editingEntryId ?: 0,
                vehicleId = vehicleId,
                dateMillis = parseDateMillis(state.refillDateText),
                odometerKm = state.odometer.toDouble(),
                amountPaid = state.amountPaid.toDouble(),
                fuelPrice = state.fuelPrice.toDouble(),
                fuelQuantity = state.fuelQuantity.toDouble(),
                fullTank = state.fullTank
            )
            if (editingEntryId == null) {
                fuelRepository.addFuelEntry(fuelEntry)
            } else {
                fuelRepository.updateEntry(fuelEntry)
            }

            vehicleRepository.updateFuelDefaults(
                vehicleId,
                state.amountPaid.toDouble(),
                state.fuelPrice.toDouble()
            )
            bypassSoftWarning = false
            _uiState.value = _uiState.value.copy(saveSuccessful = true)
        }
    }

    fun loadVehicleDefaults(vehicleId: Long) {

        if (isEditMode) return
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicleById(vehicleId)
            val lastEntry = fuelRepository.getLatestEntry(vehicleId)
            vehicle?.let {

                val hasHistory = lastEntry != null
                _uiState.value = _uiState.value.copy(
                    amountPaid =
                        if (hasHistory && it.lastAmountPaid > 0)
                            it.lastAmountPaid.toInt().toString()
                        else "",

                    fuelPrice =
                        if (hasHistory && it.lastFuelPrice > 0)
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
    fun loadEntryForEdit(entryId: Long) {
        viewModelScope.launch {

            val entry = fuelRepository.getEntryById(entryId) ?: return@launch
            isEditMode = true
            editingEntryId = entry.id
            originalOdometerKm = entry.odometerKm
            originalDateMillis = entry.dateMillis
            _uiState.value =
                _uiState.value.copy(
                    refillDateText = SimpleDateFormat(
                        "dd-MMM-yy", Locale.getDefault()
                    )
                        .format(
                            Date(entry.dateMillis)
                        ),
                    odometer = entry.odometerKm.toInt().toString(),
                    amountPaid = entry.amountPaid.toString(),
                    fuelPrice = entry.fuelPrice.toString(),
                    fuelQuantity = String.format("%.2f", entry.fuelQuantity),
                    fullTank = entry.fullTank
                )
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
            _uiState.value = _uiState.value.copy(fuelQuantity = "")
            return
        }
        val litres = amount / price
        _uiState.value = _uiState.value.copy(fuelQuantity = String.format("%.2f", litres))
    }

    fun isEditing(): Boolean {
        return editingEntryId != null
    }

    private suspend fun validateChronology(
        vehicleId: Long,
        dateMillis: Long,
        odometerKm: Double
    ): ChronologyValidationResult {
        val entries = fuelRepository.getEntriesForVehicleChronology(vehicleId)
        val filteredEntries = entries.filter { it.id != editingEntryId }

        val sameOdometerEntry =
            filteredEntries.firstOrNull {
                it.odometerKm == odometerKm
            }

        if (sameOdometerEntry != null) {
            println("DUPLICATE ODOMETER DETECTED")
            val entryNumber =
                fuelRepository.getEntryNumber(
                    vehicleId,
                    sameOdometerEntry.id
                )

            return ChronologyValidationResult(
                type = ValidationType.HARD_BLOCK,
                conflictEntryId = sameOdometerEntry.id,
                conflictEntryNumber = entryNumber,
                conflictDateMillis = sameOdometerEntry.dateMillis,
                conflictOdometerKm = sameOdometerEntry.odometerKm
            )
        }

        val previousEntry = filteredEntries
            .filter { it.odometerKm < odometerKm }
            .maxByOrNull { it.odometerKm }

        val nextEntry = filteredEntries
            .filter { it.odometerKm > odometerKm }
            .minByOrNull { it.odometerKm }
        if (
            previousEntry != null &&
            dateMillis < previousEntry.dateMillis
        ) {
            val entryNumber =
                fuelRepository.getEntryNumber(
                    vehicleId,
                    previousEntry.id
                )
            return ChronologyValidationResult(
                conflictDateMillis = previousEntry.dateMillis,
                conflictOdometerKm = previousEntry.odometerKm,
                type = ValidationType.HARD_BLOCK,
                conflictEntryId = previousEntry.id,
                conflictEntryNumber = entryNumber
            )
        }

        if (
            nextEntry != null &&
            dateMillis > nextEntry.dateMillis
        ) {
            val entryNumber =
                fuelRepository.getEntryNumber(
                    vehicleId,
                    nextEntry.id
                )
            return ChronologyValidationResult(
                conflictDateMillis = nextEntry.dateMillis,
                conflictOdometerKm = nextEntry.odometerKm,
                type = ValidationType.HARD_BLOCK,
                conflictEntryId = nextEntry.id,
                conflictEntryNumber = entryNumber
            )
        }

        if (editingEntryId == null) {

            val highestOdometer =
                filteredEntries.maxOfOrNull {
                    it.odometerKm
                }

            if (
                highestOdometer != null &&
                odometerKm < highestOdometer
            ) {
                return ChronologyValidationResult(
                    type = ValidationType.SOFT_WARNING
                )
            }
        }

        return ChronologyValidationResult(type = ValidationType.VALID)
    }

    fun clearChronologyError() {
        _uiState.value =
            _uiState.value.copy(
                chronologyError = null
            )
    }

    fun dismissSoftWarningDialog() {
        _uiState.value =
            _uiState.value.copy(
                showSoftWarningDialog = false
            )
    }

    fun confirmSoftWarningSave(
        vehicleId: Long
    ) {
        bypassSoftWarning = true
        dismissSoftWarningDialog()
        saveFuel(vehicleId)
    }
}