package com.example.mileagetracker.ui.dashboard

import com.example.mileagetracker.data.entity.Vehicle

data class DashboardUiState(
    val vehicles: List<Vehicle> = emptyList(),
    val selectedVehicle: Vehicle? = null
)