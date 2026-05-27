package com.example.mileagetracker.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: FuelGarageDatabase? = null

    fun getDatabase(
        context: Context
    ): FuelGarageDatabase {

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                FuelGarageDatabase::class.java,
                "fuel_garage.db"
            ).fallbackToDestructiveMigration(true)
                .build()

            INSTANCE = instance

            instance
        }
    }
}