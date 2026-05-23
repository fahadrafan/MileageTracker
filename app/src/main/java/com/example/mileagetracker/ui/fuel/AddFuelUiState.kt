package com.example.mileagetracker.ui.fuel

data class AddFuelUiState(
    val refillDateText: String = "",
    val refillDateMillis: Long = System.currentTimeMillis(),
    val odometer: String = "",
    val lastOdometer: String = "",
    val amountPaid: String = "",
    val fuelPrice: String = "",
    val litres: String = "",
    val fullTank: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccessful: Boolean = false
)