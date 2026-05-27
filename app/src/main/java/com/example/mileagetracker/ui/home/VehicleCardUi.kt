package com.example.mileagetracker.ui.home

data class VehicleCardUi(
    val id: Long,
    val name: String,
    val registrationNumber: String,
    val fuelType: String,
    val vehicleType: String,
    val currentMileage: Double?,
    val isDefault: Boolean
)