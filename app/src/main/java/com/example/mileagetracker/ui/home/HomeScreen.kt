package com.example.mileagetracker.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mileagetracker.data.entity.FuelType
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.ui.vehicle.VehicleDialog
import kotlinx.coroutines.launch
import com.example.mileagetracker.ui.vehicle.VehicleValidationDialog
import androidx.compose.runtime.LaunchedEffect
import com.example.mileagetracker.ui.theme.FGTypography
import com.example.mileagetracker.ui.theme.tokens.FGSpacing
import com.example.mileagetracker.ui.theme.tokens.FGScreenDefaults
import com.example.mileagetracker.ui.components.topbar.FGTopBar
import com.example.mileagetracker.ui.components.empty.FGEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vehicles: List<Vehicle>,
    mileageMap: Map<Long, Double>,
    onVehicleClick: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onAddVehicle: (
        String,
        String,
        FuelType,
        VehicleType
    ) -> Unit,

    onUpdateVehicle: (
        Long,
        String,
        String,
        FuelType,
        VehicleType
    ) -> Unit,

    onDeleteVehicle: (Long) -> Unit,
    vehicleValidationError: String?,
    onClearVehicleValidationError: () -> Unit,
    vehicleSaveSuccessful: Boolean,
    onVehicleSaveHandled: () -> Unit,
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var editingVehicleId by remember {
        mutableStateOf<Long?>(null)
    }

    var isEditMode by remember {
        mutableStateOf(false)
    }

    var vehicleName by remember {
        mutableStateOf("")
    }

    var registrationNumber by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf(VehicleType.CAR)
    }

    var selectedFuelType by remember {
        mutableStateOf(FuelType.PETROL)
    }

    var fuelTypeExpanded by remember {
        mutableStateOf(false)
    }

    var vehicleTypeExpanded by remember {
        mutableStateOf(false)
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    VehicleValidationDialog(
        error = vehicleValidationError,
        onDismiss = onClearVehicleValidationError
    )

    if (showDialog) {

        VehicleDialog(
            title =
                if (isEditMode) "Edit Vehicle"
                else "Add Vehicle",

            vehicleName = vehicleName,
            registrationNumber = registrationNumber,
            selectedFuelType = selectedFuelType,
            selectedType = selectedType,

            fuelTypeExpanded = fuelTypeExpanded,
            vehicleTypeExpanded = vehicleTypeExpanded,

            onVehicleNameChange = {
                vehicleName = it
            },

            onRegistrationChange = {
                registrationNumber = it
            },

            onFuelTypeChange = {
                selectedFuelType = it
            },

            onVehicleTypeChange = {
                selectedType = it
            },

            onFuelExpandedChange = {
                fuelTypeExpanded = it
            },

            onVehicleExpandedChange = {
                vehicleTypeExpanded = it
            },

            onSave = {
                if (isEditMode) {
                    editingVehicleId?.let { id ->

                        onUpdateVehicle(
                            id,
                            vehicleName,
                            registrationNumber,
                            selectedFuelType,
                            selectedType
                        )
                    }
                } else {
                    onAddVehicle(
                        vehicleName,
                        registrationNumber,
                        selectedFuelType,
                        selectedType
                    )
                }
            },

            onCancel = {

                vehicleName = ""
                registrationNumber = ""

                selectedFuelType = FuelType.PETROL
                selectedType = VehicleType.CAR

                isEditMode = false
                editingVehicleId = null

                showDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = { Text("Delete Vehicle") },
            text = { Text("Are you sure you want to delete this vehicle?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        editingVehicleId?.let { onDeleteVehicle(it) }
                        editingVehicleId = null
                        isEditMode = false
                        showDeleteDialog = false
                    }
                ) { Text("Delete") }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) { Text("Cancel") }
            }
        )
    }

    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.65f)
            ) {

                Spacer(
                    modifier = Modifier.height(FGSpacing.XXL)
                )

                Text(
                    text = "Fuel Garage",
                    style = FGTypography.SectionTitle,
                    modifier = Modifier.padding(FGScreenDefaults.ContentPadding)
                )

                HorizontalDivider()

                NavigationDrawerItem(

                    label = {
                        Text("Settings")
                    },

                    selected = false,

                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        onSettingsClick()
                    }

                )
            }
        }

    ) {

        Scaffold(

            topBar = {

                FGTopBar(

                    title = "My Vehicles",

                    showMenu = true,

                    onMenuClick = {

                        scope.launch {
                            drawerState.open()
                        }
                    },

                    actions = {

                        FilledIconButton(
                            onClick = {
                                isEditMode = false
                                editingVehicleId = null
                                vehicleName = ""
                                registrationNumber = ""
                                selectedFuelType = FuelType.PETROL
                                selectedType = VehicleType.CAR
                                showDialog = true
                            }
                        ) {

                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Vehicle"
                            )
                        }
                    }
                )
            }

        ) { padding ->

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(FGScreenDefaults.ContentPadding),

                verticalArrangement = Arrangement.spacedBy(FGScreenDefaults.SectionSpacing)

            ) {

                item {
                    Spacer(
                        modifier = Modifier.height(FGScreenDefaults.TopSpacing)
                    )
                }

                if (vehicles.isEmpty()) {

                    item {

                        FGEmptyState(
                            emoji = "⛽",
                            title = "No vehicles added yet",
                            subtitle = "Tap + to add your first vehicle"
                        )
                    }
                }

                items(vehicles) { vehicle ->

                    VehicleCard(
                        name = vehicle.name,
                        registrationNumber = vehicle.registrationNumber,
                        fuelType = vehicle.fuelType.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        type = vehicle.type.name,
                        mileage = mileageMap[vehicle.id],
                        onClick = {
                            onVehicleClick(vehicle.id)
                        },

                        onEdit = {

                            editingVehicleId = vehicle.id
                            isEditMode = true
                            vehicleName = vehicle.name
                            registrationNumber = vehicle.registrationNumber
                            selectedFuelType = vehicle.fuelType
                            selectedType = vehicle.type
                            showDialog = true
                        },

                        onDelete = {

                            editingVehicleId = vehicle.id
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(vehicleSaveSuccessful) {

        if (vehicleSaveSuccessful) {

            vehicleName = ""
            registrationNumber = ""

            selectedFuelType = FuelType.PETROL
            selectedType = VehicleType.CAR

            isEditMode = false
            editingVehicleId = null

            showDialog = false

            onVehicleSaveHandled()
        }
    }
}