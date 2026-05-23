package com.example.mileagetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: VehicleType,
    val lastAmountPaid: Double = 0.0,
    val lastFuelPrice: Double = 0.0
)

enum class VehicleType {
    BIKE,
    CAR
}