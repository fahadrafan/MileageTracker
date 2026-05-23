package com.example.mileagetracker.di

import android.content.Context
import com.example.mileagetracker.data.database.DatabaseProvider
import com.example.mileagetracker.data.repository.VehicleRepository

class AppContainer(context: Context) {

    private val database =
        DatabaseProvider.getDatabase(context)

    val vehicleRepository =
        VehicleRepository(
            database.vehicleDao()
        )
}