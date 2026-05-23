package com.example.mileagetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_entries")
data class FuelEntry(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val vehicleId: Long,

    val dateMillis: Long,

    val odometerKm: Double,

    val amountPaid: Double,

    val fuelPrice: Double,

    val litres: Double,

    val fullTank: Boolean
)