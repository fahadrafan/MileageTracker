package com.example.mileagetracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mileagetracker.data.entity.FuelEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FuelEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<FuelEntry>)

    @Update
    suspend fun updateEntry(
        entry: FuelEntry
    )

    @Query("""
    SELECT * FROM fuel_entries
    WHERE id = :entryId
    LIMIT 1
    """)
    suspend fun getEntryById(
        entryId: Long
    ): FuelEntry?
    @Query("""
    SELECT * FROM fuel_entries
    WHERE vehicleId = :vehicleId
    ORDER BY odometerKm DESC, id DESC
    """)
    fun getEntriesForVehicle(
        vehicleId: Long
    ): Flow<List<FuelEntry>>

    @Query("""
    SELECT * FROM fuel_entries
    WHERE vehicleId = :vehicleId
    ORDER BY odometerKm ASC, id ASC
    """)
    suspend fun getEntriesForVehicleChronology(
        vehicleId: Long
    ): List<FuelEntry>

    @Query("""
    SELECT * FROM fuel_entries
    WHERE vehicleId = :vehicleId
    ORDER BY odometerKm DESC
    """)
    suspend fun getEntriesForVehicleList(
        vehicleId: Long
    ): List<FuelEntry>

    @Query("DELETE FROM fuel_entries WHERE id = :id")
    suspend fun deleteEntry(id: Long)

    @Query("DELETE FROM fuel_entries")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM fuel_entries ORDER BY vehicleId, odometerKm, id")
    suspend fun getAllEntriesList(): List<FuelEntry>

    @Query("""
    SELECT * FROM fuel_entries
    WHERE vehicleId = :vehicleId
    ORDER BY odometerKm DESC
    LIMIT 1
    """)
    suspend fun getLatestEntry(
        vehicleId: Long
    ): FuelEntry?
}
