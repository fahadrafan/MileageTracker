package com.example.mileagetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mileagetracker.data.entity.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicle: Vehicle): Long

    @Query("SELECT * FROM vehicles ORDER BY name")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Long): Vehicle?

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteVehicle(id: Long)

    @Query("SELECT COUNT(*) FROM vehicles")
    suspend fun getVehicleCount(): Int

    @Query("""
    UPDATE vehicles
    SET lastAmountPaid = :amountPaid,
        lastFuelPrice = :fuelPrice
    WHERE id = :vehicleId
    """)
    suspend fun updateFuelDefaults(
        vehicleId: Long,
        amountPaid: Double,
        fuelPrice: Double
    )

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Query(
        """
    SELECT * FROM vehicles
    WHERE LOWER(TRIM(name)) = LOWER(TRIM(:name))
    LIMIT 1
    """
    )
    suspend fun getVehicleByName(
        name: String
    ): Vehicle?

    @Query(
        """
    SELECT * FROM vehicles
    WHERE UPPER(TRIM(registrationNumber)) =
          UPPER(TRIM(:registrationNumber))
    LIMIT 1
    """
    )
    suspend fun getVehicleByRegistration(
        registrationNumber: String
    ): Vehicle?
}