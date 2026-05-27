package com.example.mileagetracker.ui.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.entity.FuelType
import com.example.mileagetracker.data.entity.VehicleType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDialog(
    title: String,
    vehicleName: String,
    registrationNumber: String,
    selectedFuelType: FuelType,
    selectedType: VehicleType,
    fuelTypeExpanded: Boolean,
    vehicleTypeExpanded: Boolean,
    onVehicleNameChange: (String) -> Unit,
    onRegistrationChange: (String) -> Unit,
    onFuelTypeChange: (FuelType) -> Unit,
    onVehicleTypeChange: (VehicleType) -> Unit,
    onFuelExpandedChange: (Boolean) -> Unit,
    onVehicleExpandedChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,

        title = {
            Text(title)
        },

        text = {

            Column {

                OutlinedTextField(
                    value = vehicleName,
                    onValueChange = onVehicleNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Vehicle Name")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = registrationNumber,
                    onValueChange = {
                        onRegistrationChange(it.uppercase())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text("Registration Number (Optional)")
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Fuel Type")

                ExposedDropdownMenuBox(
                    expanded = fuelTypeExpanded,
                    onExpandedChange = {
                        onFuelExpandedChange(!fuelTypeExpanded)
                    }
                ) {

                    OutlinedTextField(
                        value = selectedFuelType.name
                            .lowercase()
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
                            onFuelExpandedChange(false)
                        }
                    ) {

                        FuelType.entries.forEach { fuel ->

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        fuel.name.lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    )
                                },
                                onClick = {
                                    onFuelTypeChange(fuel)
                                    onFuelExpandedChange(false)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Vehicle Type")

                ExposedDropdownMenuBox(
                    expanded = vehicleTypeExpanded,
                    onExpandedChange = {
                        onVehicleExpandedChange(!vehicleTypeExpanded)
                    }
                ) {

                    OutlinedTextField(
                        value = selectedType.name
                            .lowercase()
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
                            onVehicleExpandedChange(false)
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
                                    onVehicleTypeChange(type)
                                    onVehicleExpandedChange(false)
                                }
                            )
                        }
                    }
                }
            }
        },

        confirmButton = {
            TextButton(
                onClick = onSave
            ) {
                Text("Save")
            }
        },

        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}