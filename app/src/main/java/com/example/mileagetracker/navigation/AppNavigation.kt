package com.example.mileagetracker.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.mileagetracker.ui.dashboard.DashboardScreen
import com.example.mileagetracker.ui.fuel.AddFuelScreen
import com.example.mileagetracker.ui.history.HistoryScreen

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_FUEL = "add_fuel"
    const val HISTORY = "history"
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onAddFuelClick = {
                    navController.navigate(Routes.ADD_FUEL)
                },
                onHistoryClick = {
                    navController.navigate(Routes.HISTORY)
                }
            )
        }

        composable(Routes.ADD_FUEL) {
            AddFuelScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}