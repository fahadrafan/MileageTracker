package com.example.mileagetracker.ui.vehicle

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.entity.FuelType
import com.example.mileagetracker.data.entity.VehicleType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mileagetracker.ui.components.fields.FGOutlinedTextField
import com.example.mileagetracker.ui.components.fields.FGDropdownField

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
    var vehicleNameFocused by remember {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = onCancel,

        title = { Text(title) },

        text = {
            Column {
                FGOutlinedTextField(
                    value = vehicleName,
                    onValueChange = onVehicleNameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (
                                vehicleNameFocused &&
                                !focusState.isFocused
                            ) {
                                onVehicleNameChange(
                                    vehicleName
                                        .trim()
                                        .split("\\s+".toRegex())
                                        .joinToString(" ") {
                                            it.lowercase()
                                                .replaceFirstChar { c ->
                                                    c.uppercase()
                                                }
                                        }
                                )
                            }
                            vehicleNameFocused = focusState.isFocused
                        },
                    singleLine = true,
                    label = { Text("Vehicle Name") }
                )

                Spacer(modifier = Modifier.height(12.dp))

                FGOutlinedTextField(
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

                FGDropdownField(
                    label = {
                        Text("Fuel Type")
                    },
                    value = selectedFuelType.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    expanded = fuelTypeExpanded,
                    onExpandedChange = onFuelExpandedChange
                ) {

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

                FGDropdownField(
                    label = {
                        Text("Vehicle Type")
                    },
                    value = selectedType.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() },
                    expanded = vehicleTypeExpanded,
                    onExpandedChange = onVehicleExpandedChange
                ) {

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