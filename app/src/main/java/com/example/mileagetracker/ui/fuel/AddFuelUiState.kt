package com.example.mileagetracker.ui.fuel

data class AddFuelUiState(
    val odometer: String = "",
    val litres: String = "",
    val cost: String = "",
    val fullTank: Boolean = true
)