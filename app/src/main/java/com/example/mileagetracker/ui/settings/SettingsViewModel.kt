package com.example.mileagetracker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.backup.BackupAndRestoreManager
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.preferences.model.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit

class SettingsViewModel(
    private val preferencesRepository: UserPreferencesRepository,
    private val backupAndRestoreManager: BackupAndRestoreManager? = null
) : ViewModel() {

    val themeMode =
        preferencesRepository.themeMode
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ThemeMode.SYSTEM
            )

    val distanceUnit =
        preferencesRepository.distanceUnit
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DistanceUnit.KM
            )

    val fuelUnit =
        preferencesRepository.fuelUnit
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = FuelUnit.LITRES
            )

    val currency =
        preferencesRepository.currency
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Currency.INR
            )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            preferencesRepository.setThemeMode(mode)
        }
    }

    fun setDistanceUnit(unit: DistanceUnit) {

        viewModelScope.launch {
            preferencesRepository.setDistanceUnit(unit)
        }
    }

    fun setFuelUnit(unit: FuelUnit) {

        viewModelScope.launch {
            preferencesRepository.setFuelUnit(unit)
        }
    }

    fun setCurrency(currency: Currency) {

        viewModelScope.launch {
            preferencesRepository.setCurrency(currency)
        }
    }

    suspend fun exportBackupJson(): String {
        return requireNotNull(backupAndRestoreManager).exportJson()
    }

    suspend fun exportBackupCsv(): String {
        return requireNotNull(backupAndRestoreManager).exportCsv()
    }

    suspend fun restoreBackupJson(jsonText: String) {
        requireNotNull(backupAndRestoreManager).restore(jsonText)
    }
}

class SettingsViewModelFactory(
    private val repository: UserPreferencesRepository,
    private val backupAndRestoreManager: BackupAndRestoreManager? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                repository,
                backupAndRestoreManager
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}
