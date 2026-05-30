package com.example.mileagetracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.data.repository.FuelRepository
import com.example.mileagetracker.data.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: FuelRepository,
    private val vehicleRepository: VehicleRepository,
    private val vehicleId: Long
) : ViewModel() {

    val entries: StateFlow<List<FuelEntry>> =
        repository
            .getFuelEntriesForVehicle(vehicleId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun deleteEntry(entryId: Long) {
        viewModelScope.launch {
            repository.deleteEntry(entryId)
            val latestEntry = repository.getLatestEntry(vehicleId)

            if (latestEntry != null) {
                vehicleRepository.updateFuelDefaults(
                    vehicleId,
                    latestEntry.amountPaid,
                    latestEntry.fuelPrice
                )
            } else {
                vehicleRepository.updateFuelDefaults(
                    vehicleId,
                    0.0,
                    0.0)
            }
        }
    }
}

class HistoryViewModelFactory(
    private val repository: FuelRepository,
    private val vehicleRepository: VehicleRepository,
    private val vehicleId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {

            return HistoryViewModel(repository, vehicleRepository,vehicleId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}