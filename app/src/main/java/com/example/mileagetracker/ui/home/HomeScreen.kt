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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Alignment
import com.example.mileagetracker.data.entity.Vehicle
import com.example.mileagetracker.data.entity.VehicleType
import com.example.mileagetracker.data.entity.FuelType
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton

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

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text(
                    if (isEditMode) "Edit Vehicle"
                    else "Add Vehicle"
                )
            },

            text = {
                Column {
                    OutlinedTextField(
                        value = vehicleName,
                        onValueChange = {
                            vehicleName = it
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Vehicle Name")
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = registrationNumber,
                        onValueChange = {
                            registrationNumber = it.uppercase()
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Registration Number (Optional)")
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Fuel Type")

                    ExposedDropdownMenuBox(
                        expanded = fuelTypeExpanded,
                        onExpandedChange = {
                            fuelTypeExpanded = !fuelTypeExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedFuelType.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = fuelTypeExpanded
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = fuelTypeExpanded,
                            onDismissRequest = {
                                fuelTypeExpanded = false
                            }
                        ) {
                            FuelType.entries.forEach { fuel ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            if (fuel.name.lowercase() == "cng" ||
                                                fuel.name.lowercase() == "ev"
                                            ) fuel.name.uppercase()
                                            else fuel.name.lowercase()
                                                .replaceFirstChar { it.uppercase() })
                                    },
                                    onClick = {
                                        selectedFuelType = fuel
                                        fuelTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text("Vehicle Type")
                    ExposedDropdownMenuBox(
                        expanded = vehicleTypeExpanded,
                        onExpandedChange = {
                            vehicleTypeExpanded = !vehicleTypeExpanded
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedType.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = vehicleTypeExpanded
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = vehicleTypeExpanded,
                            onDismissRequest = {
                                vehicleTypeExpanded = false
                            }
                        ) {
                            VehicleType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            type.name.lowercase()
                                                .replaceFirstChar { it.uppercase() }
                                        )
                                    },
                                    onClick = {
                                        selectedType = type
                                        vehicleTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        if (vehicleName.isNotBlank()) {
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

                            if (alreadyExists) {
                                return@TextButton
                            }

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
                            selectedType = VehicleType.CAR
                            registrationNumber = ""
                            selectedFuelType = FuelType.PETROL
                            isEditMode = false
                            editingVehicleId = null
                            showDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        vehicleName = ""
                        registrationNumber = ""
                        selectedFuelType = FuelType.PETROL
                        selectedType = VehicleType.CAR
                        isEditMode = false
                        editingVehicleId = null
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
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