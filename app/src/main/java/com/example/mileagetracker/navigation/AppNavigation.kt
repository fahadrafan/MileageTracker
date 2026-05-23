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
            val context =
                LocalContext.current

            val app =
                context.applicationContext
                        as FuelGarageApplication

            val viewModel =
                viewModel<DashboardViewModel>(
                    factory =
                        DashboardViewModelFactory(
                            app.container.vehicleRepository
                        )
                )

            DashboardScreen(
                viewModel = viewModel,
                onAddFuelClick = {
                    navController.navigate(
                        Routes.ADD_FUEL
                    )
                },
                onHistoryClick = {
                    navController.navigate(
                        Routes.HISTORY
                    )
                }
            )
        }

        composable(Routes.ADD_FUEL) {

            val context =
                LocalContext.current

            val app =
                context.applicationContext
                        as FuelGarageApplication

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
                vehicleId = 1L,
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