package com.example.mileagetracker.ui.fuel

data class FuelEntryUiState(
    val refillDateText: String = "",
    val refillDateMillis: Long = System.currentTimeMillis(),
    val odometer: String = "",
    val lastOdometer: String = "",
    val amountPaid: String = "",
    val fuelPrice: String = "",
    val litres: String = "",
    val fullTank: Boolean = false,
    val saveSuccessful: Boolean = false,
    val dateError: String? = null,
    val odometerError: String? = null,
    val fuelQuantityError: String? = null,
    val amountPaidError: String? = null,
    val fuelPriceError: String? = null
)