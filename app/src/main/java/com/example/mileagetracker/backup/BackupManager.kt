package com.example.mileagetracker.backup

import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BackupManager(
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val json: Json = backupJson
) {
    suspend fun exportJson(): String {
        val data = BackupData(
            vehicles = vehicleRepository.getAllVehiclesList().map { it.toBackup() },
            fuelEntries = fuelRepository.getAllEntriesList().map { it.toBackup() },
            settings = preferencesRepository.getSettingsSnapshot().toBackup()
        )

        return json.encodeToString(BackupData.serializer(), data)
    }

    suspend fun exportCsv(): String {
        val vehicles = vehicleRepository.getAllVehiclesList()
        val entries = fuelRepository.getAllEntriesList()
            .groupBy { it.vehicleId }

        return buildString {
            appendLine(
                listOf(
                    "vehicle_name",
                    "registration_number",
                    "vehicle_type",
                    "fuel_type",
                    "date",
                    "odometer_km",
                    "amount_paid",
                    "fuel_price",
                    "fuel_quantity",
                    "full_tank"
                ).joinToString(",")
            )

            vehicles.forEach { vehicle ->
                val vehicleEntries = entries[vehicle.id].orEmpty()

                if (vehicleEntries.isEmpty()) {
                    appendLine(vehicleCsvPrefix(vehicle) + ",,,,,,")
                } else {
                    vehicleEntries.forEach { entry ->
                        appendLine(
                            listOf(
                                vehicle.name,
                                vehicle.registrationNumber,
                                vehicle.type.name,
                                vehicle.fuelType.name,
                                csvDate(entry.dateMillis),
                                entry.odometerKm.toString(),
                                entry.amountPaid.toString(),
                                entry.fuelPrice.toString(),
                                entry.fuelQuantity.toString(),
                                entry.fullTank.toString()
                            ).joinToString(",") { it.csvCell() }
                        )
                    }
                }
            }
        }
    }

    private fun vehicleCsvPrefix(
        vehicle: com.example.mileagetracker.data.entity.Vehicle
    ): String {
        return listOf(
            vehicle.name,
            vehicle.registrationNumber,
            vehicle.type.name,
            vehicle.fuelType.name
        ).joinToString(",") { it.csvCell() }
    }

    private fun csvDate(dateMillis: Long): String {
        return Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(csvDateFormatter)
    }

    private fun String.csvCell(): String {
        val escaped = replace("\"", "\"\"")
        return if (any { it == ',' || it == '"' || it == '\n' }) {
            "\"$escaped\""
        } else {
            escaped
        }
    }
}

internal val backupJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

private val csvDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
