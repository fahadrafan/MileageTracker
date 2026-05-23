package com.example.mileagetracker.data.database

import androidx.room.TypeConverter
import com.example.mileagetracker.data.entity.VehicleType

class TypeConverters {

    @TypeConverter
    fun fromVehicleType(type: VehicleType): String {
        return type.name
    }

    @TypeConverter
    fun toVehicleType(value: String): VehicleType {
        return VehicleType.valueOf(value)
    }
}