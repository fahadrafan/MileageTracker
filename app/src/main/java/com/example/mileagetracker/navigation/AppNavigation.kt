package com.example.mileagetracker.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.mileagetracker.ui.dashboard.DashboardScreen
import com.example.mileagetracker.ui.fuel.AddFuelScreen
import com.example.mileagetracker.ui.history.HistoryScreen
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileagetracker.FuelGarageApplication
import com.example.mileagetracker.ui.dashboard.DashboardViewModel
import com.example.mileagetracker.ui.dashboard.DashboardViewModelFactory
import com.example.mileagetracker.ui.fuel.AddFuelViewModel
import com.example.mileagetracker.ui.fuel.AddFuelViewModelFactory
import com.example.mileagetracker.ui.history.HistoryViewModel
import com.example.mileagetracker.ui.history.HistoryViewModelFactory

object Routes {
    const val DASHBOARD = "dashboard"
    const val ADD_FUEL = "add_fuel/{vehicleId}"
    const val HISTORY = "history/{vehicleId}"
    fun addFuel(vehicleId: Long): String {
        return "add_fuel/$vehicleId"
    }

    fun history(vehicleId: Long): String {
        return "history/$vehicleId"
    }
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(Routes.DASHBOARD) {
            val context = LocalContext.current

            val app = context.applicationContext as FuelGarageApplication

            val viewModel = viewModel<DashboardViewModel>(
                factory = DashboardViewModelFactory(
                    app.container.vehicleRepository,
                    app.container.fuelRepository
                )
            )

            DashboardScreen(
                viewModel = viewModel,
                onAddFuelClick = {

                    val vehicleId =
                        viewModel.uiState.value.selectedVehicle?.id

                    if (vehicleId != null) {
                        navController.navigate(
                            Routes.addFuel(vehicleId)
                        )
                    }
                },
                onHistoryClick = {
                    val vehicleId = viewModel.uiState.value.selectedVehicle?.id
                    if (vehicleId != null) {
                        navController.navigate(Routes.history(vehicleId))
                    }
                }
            )
        }

        composable("add_fuel/{vehicleId}") { backStackEntry ->
            val vehicleId =
                backStackEntry.arguments
                    ?.getString("vehicleId")
                    ?.toLongOrNull()
                    ?: return@composable

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication
            val viewModel =
                viewModel<AddFuelViewModel>(
                    factory =
                        AddFuelViewModelFactory(
                            app.container.fuelRepository,
                            app.container.vehicleRepository
                        )
                )
            AddFuelScreen(
                viewModel = viewModel,
                vehicleId = vehicleId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.HISTORY) { backStackEntry ->

            val vehicleId =
                backStackEntry.arguments
                    ?.getString("vehicleId")
                    ?.toLongOrNull()
                    ?: return@composable

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication
            val historyViewModel =
                viewModel<HistoryViewModel>(
                    factory =
                        HistoryViewModelFactory(
                            app.container.fuelRepository,
                            vehicleId
                        )
                )
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}