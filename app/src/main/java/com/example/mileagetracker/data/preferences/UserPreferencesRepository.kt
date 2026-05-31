package com.example.mileagetracker.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.mileagetracker.data.preferences.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit

class UserPreferencesRepository(
    private val context: Context
) {

    val themeMode: Flow<ThemeMode> =
        context.dataStore.data.map { preferences ->

            when (
                preferences[PreferenceKeys.THEME_MODE]
            ) {
                ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                ThemeMode.DARK.name -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }

    val distanceUnit: Flow<DistanceUnit> =
        context.dataStore.data.map { preferences ->

            when (
                preferences[PreferenceKeys.DISTANCE_UNIT]
            ) {

                DistanceUnit.MILES.name -> DistanceUnit.MILES

                else -> DistanceUnit.KM
            }
        }

    val fuelUnit: Flow<FuelUnit> =
        context.dataStore.data.map { preferences ->

            when (
                preferences[PreferenceKeys.FUEL_UNIT]
            ) {

                FuelUnit.GALLONS.name -> FuelUnit.GALLONS

                else -> FuelUnit.LITRES
            }
        }

    val currency: Flow<Currency> =
        context.dataStore.data.map { preferences ->

            val savedCurrency =
                preferences[PreferenceKeys.CURRENCY]

            Currency.entries.find {
                it.name == savedCurrency
            } ?: Currency.INR
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setDistanceUnit(
        unit: DistanceUnit
    ) {

        context.dataStore.edit { preferences ->

            preferences[PreferenceKeys.DISTANCE_UNIT] =
                unit.name
        }
    }

    suspend fun setFuelUnit(
        unit: FuelUnit
    ) {

        context.dataStore.edit { preferences ->

            preferences[PreferenceKeys.FUEL_UNIT] =
                unit.name
        }
    }

    suspend fun setCurrency(
        currency: Currency
    ) {

        context.dataStore.edit { preferences ->

            preferences[PreferenceKeys.CURRENCY] =
                currency.name
        }
    }

}