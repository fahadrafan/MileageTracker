package com.example.mileagetracker.backup

import androidx.room.withTransaction
import com.example.mileagetracker.data.database.FuelGarageDatabase
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class BackupAndRestoreManager(
    private val database: FuelGarageDatabase,
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val json: Json = backupJson
) {
    suspend fun exportJson(): String {
        val vehicles = vehicleRepository.getAllVehiclesList()

        if (vehicles.isEmpty()) {
            throw EmptyBackupDataError()
        }

        val data = BackupData(
            version = BackupVersion.CURRENT,
            vehicles = vehicles.map { it.toBackup() },
            fuelEntries = fuelRepository.getAllEntriesList().map { it.toBackup() },
            settings = preferencesRepository.getSettingsSnapshot().toBackup()
        )

        return json.encodeToString(BackupData.serializer(), data)
    }

    suspend fun exportCsv(): String {
        val vehicles = vehicleRepository.getAllVehiclesList()

        if (vehicles.isEmpty()) {
            throw EmptyBackupDataError()
        }

        val entries = fuelRepository.getAllEntriesList()
            .groupBy { it.vehicleId }

        return buildString {
            appendLine(csvHeader)

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

    suspend fun restore(jsonText: String) {
        val backup = try {
            json.decodeFromString(BackupData.serializer(), jsonText)
        } catch (error: SerializationException) {
            throw InvalidBackupFileError(error)
        } catch (error: IllegalArgumentException) {
            throw InvalidBackupFileError(error)
        }

        if (!BackupVersion.isSupported(backup.version)) {
            throw UnsupportedBackupVersionError(backup.version)
        }

        if (backup.vehicles.isEmpty()) {
            throw EmptyRestoreDataError()
        }

        try {
            database.withTransaction {
                fuelRepository.deleteAllEntries()
                vehicleRepository.deleteAllVehicles()
                vehicleRepository.addVehicles(backup.vehicles.map { it.toEntity() })
                fuelRepository.addFuelEntries(backup.fuelEntries.map { it.toEntity() })
                preferencesRepository.restoreSettings(backup.settings.toAppSettings())
            }
        } catch (error: Exception) {
            throw RestoreFailedError(error)
        }
    }

    private fun vehicleCsvPrefix(vehicle: Vehicle): String {
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
    encodeDefaults = true
}

private val csvDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

private val csvHeader = listOf(
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
