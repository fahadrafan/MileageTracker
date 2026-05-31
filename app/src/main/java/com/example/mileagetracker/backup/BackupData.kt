package com.example.mileagetracker.backup

import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.data.entity.FuelType
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.preferences.AppSettings
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit
import com.example.mileagetracker.data.preferences.model.ThemeMode
import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = BackupVersion.CURRENT,
    val vehicles: List<BackupVehicle>,
    val fuelEntries: List<BackupFuelEntry>,
    val settings: BackupSettings
)

@Serializable
data class BackupVehicle(
    val id: Long,
    val name: String,
    val registrationNumber: String,
    val fuelType: FuelType,
    val type: VehicleType,
    val lastAmountPaid: Double,
    val lastFuelPrice: Double
)

@Serializable
data class BackupFuelEntry(
    val id: Long,
    val vehicleId: Long,
    val dateMillis: Long,
    val odometerKm: Double,
    val amountPaid: Double,
    val fuelPrice: Double,
    val fuelQuantity: Double,
    val fullTank: Boolean
)

@Serializable
data class BackupSettings(
    val themeMode: ThemeMode,
    val distanceUnit: DistanceUnit,
    val fuelUnit: FuelUnit,
    val currency: Currency
)

fun Vehicle.toBackup() = BackupVehicle(
    id, name, registrationNumber, fuelType, type, lastAmountPaid, lastFuelPrice
)

fun BackupVehicle.toEntity() = Vehicle(
    id, name, registrationNumber, fuelType, type, lastAmountPaid, lastFuelPrice
)

fun FuelEntry.toBackup() = BackupFuelEntry(
    id, vehicleId, dateMillis, odometerKm, amountPaid, fuelPrice, fuelQuantity, fullTank
)

fun BackupFuelEntry.toEntity() = FuelEntry(
    id, vehicleId, dateMillis, odometerKm, amountPaid, fuelPrice, fuelQuantity, fullTank
)

fun AppSettings.toBackup() = BackupSettings(themeMode, distanceUnit, fuelUnit, currency)

fun BackupSettings.toAppSettings() = AppSettings(themeMode, distanceUnit, fuelUnit, currency)
