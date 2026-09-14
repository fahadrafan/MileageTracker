package com.example.mileagetracker

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.SystemBarStyle
import com.example.mileagetracker.navigation.AppNavigation
import com.example.mileagetracker.ui.theme.FuelGarageTheme
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.preferences.model.ThemeMode
import com.example.mileagetracker.ui.settings.SettingsViewModel
import com.example.mileagetracker.ui.settings.SettingsViewModelFactory

class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            UserPreferencesRepository(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val themeMode by settingsViewModel.themeMode.collectAsState()

            val darkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            LaunchedEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = if (darkTheme) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT
                        )
                    },
                    navigationBarStyle = if (darkTheme) {
                        SystemBarStyle.dark(Color.TRANSPARENT)
                    } else {
                        SystemBarStyle.light(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT
                        )
                    }
                )
            }

            FuelGarageTheme(
                darkTheme = darkTheme
            ) {
                AppNavigation()
            }
        }
    }
}