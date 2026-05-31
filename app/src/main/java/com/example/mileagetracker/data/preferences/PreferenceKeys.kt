package com.example.mileagetracker.data.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {

    val THEME_MODE =
        stringPreferencesKey("theme_mode")

    val DISTANCE_UNIT =
        stringPreferencesKey("distance_unit")

    val FUEL_UNIT =
        stringPreferencesKey("fuel_unit")

    val CURRENCY =
        stringPreferencesKey("currency")
}