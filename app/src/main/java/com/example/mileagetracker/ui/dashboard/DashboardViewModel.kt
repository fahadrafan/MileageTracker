package com.example.mileagetracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.entity.FuelType
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import com.example.mileagetracker.utils.MileageCalculator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    private var statisticsJob: Job? = null
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        vehicleRepository.getVehicles().onEach { vehicles ->
            val currentSelection = _uiState.value.selectedVehicle
            val selectedVehicle = vehicles.find {
                it.id == currentSelection?.id
            } ?: vehicles.firstOrNull()

            _uiState.value =
                _uiState.value.copy(vehicles = vehicles, selectedVehicle = selectedVehicle)

            viewModelScope.launch {
                val mileageMap = mutableMapOf<Long, Double>()
                vehicles.forEach { vehicle ->
                    val entries = fuelRepository.getFuelEntriesForVehicle(vehicle.id).first()
                    mileageMap[vehicle.id] =
                        MileageCalculator
                            .calculateStatistics(entries)
                            .estimatedMileage
                }
                _uiState.value =
                    _uiState.value.copy(
                        vehicleMileageMap = mileageMap
                    )
            }

            selectedVehicle?.let { observeStatistics(it.id) }
        }
            .launchIn(viewModelScope)
    }

    fun addVehicle(
        name: String,
        registrationNumber: String,
        fuelType: FuelType,
        type: VehicleType
    ) {
        viewModelScope.launch {
            val formattedName = formatVehicleName(name)
            val trimmedRegistration = registrationNumber.trim()
            val error =
                validateVehicle(
                    vehicleId = null,
                    name = formattedName,
                    registrationNumber = trimmedRegistration
                )
            if (error != null) {
                _uiState.value =
                    _uiState.value.copy(
                        vehicleValidationError = error
                    )
                return@launch
            }
            val vehicle = Vehicle(
                name = formattedName,
                registrationNumber = trimmedRegistration,
                fuelType = fuelType,
                type = type
            )

            val vehicleId = vehicleRepository.addVehicle(vehicle)

            selectVehicle(vehicle.copy(id = vehicleId))
            _uiState.value =
                _uiState.value.copy(
                    vehicleSaveSuccessful = true
                )
        }
    }

    fun deleteVehicle(vehicleId: Long) {
        viewModelScope.launch {
            vehicleRepository.deleteVehicle(vehicleId)
        }
    }

    fun updateVehicle(
        vehicleId: Long,
        name: String,
        registrationNumber: String,
        fuelType: FuelType,
        type: VehicleType
    ) {
        viewModelScope.launch {
            val formattedName = formatVehicleName(name)
            val trimmedRegistration = registrationNumber.trim()
            val error =
                validateVehicle(
                    vehicleId = vehicleId,
                    name = formattedName,
                    registrationNumber = trimmedRegistration
                )
            if (error != null) {
                _uiState.value =
                    _uiState.value.copy(
                        vehicleValidationError = error
                    )
                return@launch
            }
            val vehicle = Vehicle(
                id = vehicleId,
                name = formattedName,
                registrationNumber = trimmedRegistration,
                fuelType = fuelType,
                type = type
            )
            vehicleRepository.updateVehicle(vehicle)
            _uiState.value =
                _uiState.value.copy(
                    vehicleSaveSuccessful = true
                )
        }
    }

    fun selectVehicle(vehicle: Vehicle) {
        _uiState.value = _uiState.value.copy(selectedVehicle = vehicle)
        observeStatistics(vehicle.id)
    }

    fun selectVehicleById(vehicleId: Long) {
        viewModelScope.launch {
            vehicleRepository.getVehicleById(vehicleId)?.let {
                selectVehicle(it)
            }
        }
    }

    private fun observeStatistics(vehicleId: Long) {
        statisticsJob?.cancel()
        statisticsJob = viewModelScope.launch {
            fuelRepository
                .getFuelEntriesForVehicle(vehicleId)
                .collectLatest { entries ->
                    val stats = MileageCalculator.calculateStatistics(entries)

                    _uiState.value = _uiState.value.copy(
                        statistics = stats,
                        recentEntries = entries
                            .sortedByDescending { it.dateMillis }
                            .take(3),
                    )
                }
        }
    }

    fun clearVehicleValidationError() {
        _uiState.value =
            _uiState.value.copy(
                vehicleValidationError = null
            )
    }

    fun clearVehicleSaveSuccessful() {
        _uiState.value =
            _uiState.value.copy(
                vehicleSaveSuccessful = false
            )
    }

    private fun formatVehicleName(name: String): String {
        return name
            .trim()
            .split("\\s+".toRegex())
            .joinToString(" ") {
                it.lowercase()
                    .replaceFirstChar { c ->
                        c.uppercase()
                    }
            }
    }

    private suspend fun validateVehicle(
        vehicleId: Long?,
        name: String,
        registrationNumber: String
    ): String? {

        if (name.isBlank()) {
            return "Vehicle name cannot be empty."
        }

        val existingName =
            vehicleRepository.getVehicleByName(name)

        if (
            existingName != null &&
            existingName.id != vehicleId
        ) {
            return "Vehicle name already exists.\n\nPlease choose a different name."
        }

        if (registrationNumber.isNotBlank()) {

            val existingRegistration =
                vehicleRepository.getVehicleByRegistration(
                    registrationNumber
                )

            if (
                existingRegistration != null &&
                existingRegistration.id != vehicleId
            ) {
                return "Registration number already exists.\n\nPlease enter a different registration number."
            }
        }

        return null
    }
}