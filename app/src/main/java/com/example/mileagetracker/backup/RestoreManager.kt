package com.example.mileagetracker.backup

import androidx.room.withTransaction
import com.example.mileagetracker.data.database.FuelGarageDatabase
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.serialization.json.Json

class RestoreManager(
    private val database: FuelGarageDatabase,
    private val vehicleRepository: VehicleRepository,
    private val fuelRepository: FuelRepository,
    private val preferencesRepository: UserPreferencesRepository,
    private val json: Json = backupJson
) {
    suspend fun restore(jsonText: String) {
        val backup = json.decodeFromString(BackupData.serializer(), jsonText)

        require(BackupVersion.isSupported(backup.version)) {
            "Unsupported backup version: ${backup.version}"
        }

        database.withTransaction {
            fuelRepository.deleteAllEntries()
            vehicleRepository.deleteAllVehicles()
            vehicleRepository.addVehicles(backup.vehicles.map { it.toEntity() })
            fuelRepository.addFuelEntries(backup.fuelEntries.map { it.toEntity() })
            preferencesRepository.restoreSettings(backup.settings.toAppSettings())
        }
    }
}
