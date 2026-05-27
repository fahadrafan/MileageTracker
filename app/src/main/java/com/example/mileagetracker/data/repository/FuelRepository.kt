package com.example.mileagetracker.data.repository

import com.example.mileagetracker.data.dao.FuelEntryDao
import com.example.mileagetracker.data.entity.FuelEntry
import kotlinx.coroutines.flow.Flow

class FuelRepository(
    private val fuelEntryDao: FuelEntryDao
) {

    suspend fun getEntriesForVehicleList(
        vehicleId: Long
    ): List<FuelEntry> {
        return fuelEntryDao.getEntriesForVehicleList(vehicleId)
    }
    suspend fun addFuelEntry(fuelEntry: FuelEntry) {
        fuelEntryDao.insert(fuelEntry)
    }

    suspend fun updateEntry(entry: FuelEntry) {
        fuelEntryDao.updateEntry(entry)
    }

    suspend fun getEntryById(entryId: Long): FuelEntry? {
        return fuelEntryDao.getEntryById(entryId)
    }

    fun getFuelEntriesForVehicle(vehicleId: Long): Flow<List<FuelEntry>> {
        return fuelEntryDao.getEntriesForVehicle(vehicleId)
    }

    suspend fun getLatestEntry(vehicleId: Long): FuelEntry? {
        return fuelEntryDao.getLatestEntry(vehicleId)
    }

    suspend fun deleteEntry(entryId: Long) {
        fuelEntryDao.deleteEntry(entryId)
    }
}