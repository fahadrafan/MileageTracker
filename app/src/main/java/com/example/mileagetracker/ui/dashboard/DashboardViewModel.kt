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
            val selectedVehicle = _uiState.value.selectedVehicle ?: vehicles.firstOrNull()
            _uiState.value =
                _uiState.value.copy(vehicles = vehicles, selectedVehicle = selectedVehicle)
            selectedVehicle?.let { observeStatistics(it.id) }
        }
            .launchIn(viewModelScope)
    }

    fun addVehicle(name: String, type: VehicleType) {
        viewModelScope.launch {
            vehicleRepository.addVehicle(
                Vehicle(
                    name = name,
                    type = type
                )
            )
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
                    android.util.Log.d(
                        "FuelGarageStats",
                        """
    VehicleId=$vehicleId
    Entries=${entries.size}
    Estimated=${stats.estimatedMileage}
    LastVerified=${stats.lastVerifiedMileage}
    AverageVerified=${stats.averageVerifiedMileage}
    Distance=${stats.totalDistance}
    Fuel=${stats.fuelConsumed}
    Spent=${stats.totalSpent}
    CostPerKm=${stats.costPerKm}
    """.trimIndent()
                    )
                    _uiState.value = _uiState.value.copy(statistics = stats)
                }
        }
    }
}