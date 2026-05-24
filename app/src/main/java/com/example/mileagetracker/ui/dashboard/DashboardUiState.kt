package com.example.mileagetracker.ui.dashboard

import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.utils.VehicleStatistics

data class DashboardUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: Vehicle? = null,
    val statistics: VehicleStatistics = VehicleStatistics()
)