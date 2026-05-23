package com.example.mileagetracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mileagetracker.data.dao.FuelEntryDao
import com.example.mileagetracker.data.dao.VehicleDao
import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.data.entity.Vehicle

@Database(
    entities = [
        Vehicle::class,
        FuelEntry::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    com.example.mileagetracker.data.database.TypeConverters::class
)
abstract class FuelGarageDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    abstract fun fuelEntryDao(): FuelEntryDao
}