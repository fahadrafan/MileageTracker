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
    ): Long {
        return vehicleDao.insert(vehicle)
    }

    suspend fun addVehicles(vehicles: List<Vehicle>) {
        vehicleDao.insertAll(vehicles)
    }

    suspend fun getAllVehiclesList(): List<Vehicle> {
        return vehicleDao.getAllVehiclesList()
    }

    suspend fun deleteVehicle(
        vehicleId: Long
    ) {
        vehicleDao.deleteVehicle(vehicleId)
    }

    suspend fun deleteAllVehicles() {
        vehicleDao.deleteAllVehicles()
    }

    suspend fun getVehicleById(id: Long): Vehicle? {
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

    suspend fun updateVehicle(vehicle: Vehicle) {
        vehicleDao.updateVehicle(vehicle)
    }

    suspend fun getVehicleByName(name: String): Vehicle? {
        return vehicleDao.getVehicleByName(name)
    }

    suspend fun getVehicleByRegistration(registrationNumber: String): Vehicle? {
        return vehicleDao.getVehicleByRegistration(registrationNumber)
    }
}
