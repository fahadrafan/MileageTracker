package com.example.mileagetracker.utils

data class VehicleStatistics(
    val estimatedMileage: Double = 0.0,
    val lastVerifiedMileage: Double = 0.0,
    val averageVerifiedMileage: Double = 0.0,
    val totalDistance: Double = 0.0,
    val fuelConsumed: Double = 0.0,
    val totalSpent: Double = 0.0,
    val costPerKm: Double = 0.0,

    val fuelEntryCount: Int = 0,
    val lastRefuelDate: Long = 0L,
    val lastRefuelCost: Double = 0.0,
    val lastRefuelLitres: Double = 0.0,
    val lastRefuelFullTank: Boolean = false
)