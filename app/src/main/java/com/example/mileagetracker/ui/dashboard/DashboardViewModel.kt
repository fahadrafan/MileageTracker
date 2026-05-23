package com.example.mileagetracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: VehicleRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DashboardUiState())

    val uiState: StateFlow<DashboardUiState> =
        _uiState.asStateFlow()

    init {

        repository
            .getVehicles()
            .onEach { vehicles ->

                _uiState.value =
                    DashboardUiState(
                        vehicles = vehicles
                    )
            }
            .launchIn(viewModelScope)
    }

    fun addSampleVehicle() {

        viewModelScope.launch {

            repository.addVehicle(
                Vehicle(
                    name = "Hyundai i20",
                    type = VehicleType.CAR
                )
            )
        }
    }
}