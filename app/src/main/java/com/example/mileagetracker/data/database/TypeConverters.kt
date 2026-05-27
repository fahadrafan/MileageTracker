package com.example.mileagetracker.data.database

import androidx.room.TypeConverter
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.entity.FuelType

class TypeConverters {

    @TypeConverter
    fun fromVehicleType(type: VehicleType): String {
        return type.name
    }

    @TypeConverter
    fun toVehicleType(value: String): VehicleType {
        return VehicleType.valueOf(value)
    }

    @TypeConverter
    fun fromFuelType(type: FuelType): String {
        return type.name
    }

    @TypeConverter
    fun toFuelType(value: String): FuelType {
        return FuelType.valueOf(value)
    }
}