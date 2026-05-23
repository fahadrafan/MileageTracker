package com.example.mileagetracker.data.repository

import com.example.mileagetracker.data.dao.FuelEntryDao
import com.example.mileagetracker.data.entity.FuelEntry
import kotlinx.coroutines.flow.Flow

class FuelRepository(
    private val fuelEntryDao: FuelEntryDao
) {

    suspend fun addFuelEntry(
        fuelEntry: FuelEntry
    ) {
        fuelEntryDao.insert(fuelEntry)
    }

    fun getEntries(
        vehicleId: Long
    ): Flow<List<FuelEntry>> {

        return fuelEntryDao
            .getEntriesForVehicle(vehicleId)
    }

    suspend fun getLatestEntry(
        vehicleId: Long
    ): FuelEntry? {

        return fuelEntryDao
            .getLatestEntry(vehicleId)
    }
}