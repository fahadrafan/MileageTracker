package com.example.mileagetracker.ui.fuel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository

class FuelEntryViewModelFactory(
    private val fuelRepository: FuelRepository,
    private val vehicleRepository: VehicleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
            modelClass: Class<T>
    ): T {

        if (
                modelClass.isAssignableFrom(
                        FuelEntryViewModel::class.java
            )
        ) {

            return FuelEntryViewModel(
                fuelRepository,
                vehicleRepository
            ) as T
        }

        throw IllegalArgumentException(
                "Unknown ViewModel"
        )
    }
}