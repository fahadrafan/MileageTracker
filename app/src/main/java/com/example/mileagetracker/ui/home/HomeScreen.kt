package com.example.mileagetracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.Alignment
import com.example.mileagetracker.ui.vehicle.VehicleDialog
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.entity.FuelType
import androidx.compose.material.icons.filled.Add

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vehicles: List<Vehicle>,
    mileageMap: Map<Long, Double>,
    onVehicleClick: (Long) -> Unit,
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

    onDeleteVehicle: (Long) -> Unit
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

                if (vehicleName.isBlank()) return@VehicleDialog

                val alreadyExists =
                    vehicles.any {

                        if (isEditMode) {
                            it.id != editingVehicleId &&
                                    it.name.equals(
                                        vehicleName.trim(),
                                        ignoreCase = true
                                    )
                        } else {
                            it.name.equals(
                                vehicleName.trim(),
                                ignoreCase = true
                            )
                        }
                    }

                if (alreadyExists) return@VehicleDialog

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

                vehicleName = ""
                registrationNumber = ""
                selectedFuelType = FuelType.PETROL
                selectedType = VehicleType.CAR

                isEditMode = false
                editingVehicleId = null

                showDialog = false
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

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "My Vehicles",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    FilledIconButton(
                        onClick = {
                            isEditMode = false
                            editingVehicleId = null
                            vehicleName = ""
                            registrationNumber = ""
                            selectedFuelType = FuelType.PETROL
                            selectedType = VehicleType.CAR
                            isEditMode = false
                            editingVehicleId = null
                            showDialog = true
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Vehicle"
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            if (vehicles.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "⛽",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )
                            Text(
                                text = "No vehicles added yet",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                            Text(
                                text = "Tap + to add your first vehicle",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
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
                    onClick = { onVehicleClick(vehicle.id) },
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