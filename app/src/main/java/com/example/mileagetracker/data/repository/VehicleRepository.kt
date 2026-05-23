package com.example.mileagetracker.data.repository

import com.example.mileagetracker.data.dao.VehicleDao
import com.example.mileagetracker.data.entity.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val vehicleDao: VehicleDao
) {

    fun getVehicles(): Flow<List<Vehicle>> {
        return vehicleDao.getAllVehicles()
    }

    suspend fun addVehicle(
        vehicle: Vehicle
    ) {
        vehicleDao.insert(vehicle)
    }

    suspend fun deleteVehicle(
        vehicleId: Long
    ) {
        vehicleDao.deleteVehicle(vehicleId)
    }

    suspend fun getVehicleById(
        id: Long
    ): Vehicle? {

        return vehicleDao.getVehicleById(id)
    }

    suspend fun updateFuelDefaults(
        vehicleId: Long,
        amountPaid: Double,
        fuelPrice: Double
    ) {

        vehicleDao.updateFuelDefaults(
            vehicleId,
            amountPaid,
            fuelPrice
        )
    }
}