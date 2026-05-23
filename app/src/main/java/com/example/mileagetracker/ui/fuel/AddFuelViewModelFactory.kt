package com.example.mileagetracker.ui.fuel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mileagetracker.data.repository.FuelRepository

class AddFuelViewModelFactory(
        private val repository: FuelRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
            modelClass: Class<T>
    ): T {

        if (
                modelClass.isAssignableFrom(
                        AddFuelViewModel::class.java
            )
        ) {

            return AddFuelViewModel(
                    repository
            ) as T
        }

        throw IllegalArgumentException(
                "Unknown ViewModel"
        )
    }
}