package com.example.mileagetracker.data.repository

import com.example.mileagetracker.data.dao.VehicleDao
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType

suspend fun seedVehicles(
    vehicleDao: VehicleDao
) {

    if (vehicleDao.getVehicleCount() > 0) {
        return
    }

    vehicleDao.insert(
        Vehicle(
            name = "Hyundai i20",
            type = VehicleType.CAR
        )
    )

    vehicleDao.insert(
        Vehicle(
            name = "Honda Shine",
            type = VehicleType.BIKE
        )
    )
}