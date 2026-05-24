package com.example.mileagetracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

            selectedVehicle?.let { observeStatistics(it.id) }
        }
            .launchIn(viewModelScope)
    }

    fun addVehicle(name: String, type: VehicleType) {
        viewModelScope.launch {
            val vehicle = Vehicle(
                name = name,
                type = type
            )
            val vehicleId = vehicleRepository.addVehicle(vehicle)
            selectVehicle(vehicle.copy(id = vehicleId))
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
        type: VehicleType
    ) {
        viewModelScope.launch {
            val vehicle = Vehicle(
                id = vehicleId,
                name = name,
                type = type
            )
            vehicleRepository.updateVehicle(vehicle)
        }
    }

    fun selectVehicle(vehicle: Vehicle) {
        _uiState.value = _uiState.value.copy(selectedVehicle = vehicle)
        observeStatistics(vehicle.id)
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
                            .take(3)
                    )
                }
        }
    }
}