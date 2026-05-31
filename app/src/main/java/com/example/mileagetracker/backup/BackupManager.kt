package com.example.mileagetracker.backup

import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.serialization.json.Json

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
}

internal val backupJson = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}
