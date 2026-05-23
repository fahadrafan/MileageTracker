package com.example.mileagetracker.ui.fuel

data class AddFuelUiState(
    val refillDateMillis: Long = System.currentTimeMillis(),
    val odometer: String = "",
    val amountPaid: String = "",
    val fuelPrice: String = "",
    val litres: String = "",
    val fullTank: Boolean = true
)