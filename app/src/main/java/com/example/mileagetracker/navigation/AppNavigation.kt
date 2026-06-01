package com.example.mileagetracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mileagetracker.FuelGarageApplication
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.ui.dashboard.DashboardScreen
import com.example.mileagetracker.ui.dashboard.DashboardViewModel
import com.example.mileagetracker.ui.dashboard.DashboardViewModelFactory
import com.example.mileagetracker.ui.fuel.FuelEntryScreen
import com.example.mileagetracker.ui.fuel.FuelEntryViewModel
import com.example.mileagetracker.ui.fuel.FuelEntryViewModelFactory
import com.example.mileagetracker.ui.history.HistoryScreen
import com.example.mileagetracker.ui.history.HistoryViewModel
import com.example.mileagetracker.ui.history.HistoryViewModelFactory
import com.example.mileagetracker.ui.home.HomeScreen
import com.example.mileagetracker.ui.settings.SettingsScreen
import com.example.mileagetracker.ui.settings.SettingsViewModel
import com.example.mileagetracker.ui.settings.SettingsViewModelFactory

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val VEHICLE_DASHBOARD = "vehicle_dashboard/{vehicleId}"
    const val ADD_FUEL = "add_fuel/{vehicleId}"
    const val HISTORY = "history/{vehicleId}"
    const val EDIT_FUEL = "edit_fuel/{vehicleId}/{entryId}"

    fun vehicleDashboard(vehicleId: Long): String {
        return "vehicle_dashboard/$vehicleId"
    }
    fun addFuel(vehicleId: Long): String {
        return "add_fuel/$vehicleId"
    }

    fun history(vehicleId: Long): String {
        return "history/$vehicleId"
    }

    fun editFuel(vehicleId: Long, entryId: Long): String {
        return "edit_fuel/$vehicleId/$entryId"
    }
}


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication

            val viewModel = viewModel<DashboardViewModel>(
                factory = DashboardViewModelFactory(
                    app.container.vehicleRepository,
                    app.container.fuelRepository
                )
            )

            val uiState by viewModel.uiState.collectAsState()

            HomeScreen(
                vehicles = uiState.vehicles,
                mileageMap = uiState.vehicleMileageMap,
                vehicleValidationError = uiState.vehicleValidationError,
                onClearVehicleValidationError = {
                    viewModel.clearVehicleValidationError()
                },
                onVehicleClick = { vehicleId ->
                    navController.navigate(
                        Routes.vehicleDashboard(vehicleId)
                    )
                },
                onAddVehicle = { name, registrationNumber, fuelType, type ->
                    viewModel.addVehicle(
                        name = name,
                        registrationNumber = registrationNumber,
                        fuelType = fuelType,
                        type = type
                    )
                },
                onUpdateVehicle = {
                        id,
                        name,
                        registrationNumber,
                        fuelType,
                        type ->

                    viewModel.updateVehicle(
                        vehicleId = id,
                        name = name,
                        registrationNumber = registrationNumber,
                        fuelType = fuelType,
                        type = type
                    )
                },
                onDeleteVehicle = { vehicleId ->
                    viewModel.deleteVehicle(vehicleId)
                },
                onSettingsClick = {
                    navController.navigate("settings")
                },
                vehicleSaveSuccessful =
                    uiState.vehicleSaveSuccessful,

                onVehicleSaveHandled = {
                    viewModel.clearVehicleSaveSuccessful()
                },
            )
        }

        composable("settings") {

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication

            val viewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(
                    app.container.preferencesRepository,
                    app.container.backupAndRestoreManager
                )
            )

            SettingsScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.VEHICLE_DASHBOARD
        ) { backStackEntry ->

            val vehicleId =
                backStackEntry.arguments
                    ?.getString("vehicleId")
                    ?.toLongOrNull()
                    ?: return@composable

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication

            val viewModel = viewModel<DashboardViewModel>(
                factory = DashboardViewModelFactory(
                    app.container.vehicleRepository,
                    app.container.fuelRepository
                )
            )

            LaunchedEffect(vehicleId) {
                viewModel.selectVehicleById(vehicleId)
            }

            DashboardScreen(
                viewModel = viewModel,
                onAddFuelClick = {
                    navController.navigate(
                        Routes.addFuel(vehicleId)
                    )
                },
                onHistoryClick = {
                    navController.navigate(
                        Routes.history(vehicleId)
                    )
                },
                onBack = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onVehicleDeleted = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) {
                            inclusive = false
                        }
                        launchSingleTop = true
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
                viewModel<FuelEntryViewModel>(
                    factory =
                        FuelEntryViewModelFactory(
                            app.container.fuelRepository,
                            app.container.vehicleRepository
                        )
                )
            FuelEntryScreen(
                viewModel = viewModel,
                vehicleId = vehicleId,
                isEditMode = false,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.EDIT_FUEL) { backStackEntry ->
            val vehicleId =
                backStackEntry.arguments
                    ?.getString("vehicleId")
                    ?.toLongOrNull()
                    ?: return@composable

            val entryId =
                backStackEntry.arguments
                    ?.getString("entryId")
                    ?.toLongOrNull()
                    ?: return@composable

            val context = LocalContext.current
            val app = context.applicationContext as FuelGarageApplication

            val viewModel =
                viewModel<FuelEntryViewModel>(
                    factory =
                        FuelEntryViewModelFactory(
                            app.container.fuelRepository,
                            app.container.vehicleRepository
                        )
                )

            LaunchedEffect(entryId) {
                viewModel.loadEntryForEdit(entryId)
            }

            FuelEntryScreen(
                viewModel = viewModel,
                vehicleId = vehicleId,
                isEditMode = true,
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
                            app.container.vehicleRepository,
                            vehicleId
                        )
                )
            HistoryScreen(
                viewModel = historyViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onEditEntry = { entry ->
                    navController.navigate(
                        Routes.editFuel(
                            vehicleId = vehicleId,
                            entryId = entry.id
                        )
                    )
                },
                onAddFuel = {
                    navController.navigate(
                        Routes.addFuel(vehicleId)
                    )
                }
            )
        }
    }
}
