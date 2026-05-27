package com.example.mileagetracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun HomeScreen(
    vehicles: List<Vehicle>,
    onVehicleClick: (Long) -> Unit,
    onAddVehicle: (String, VehicleType) -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var vehicleName by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf(VehicleType.CAR)
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text("Add Vehicle")
            },

            text = {

                Column {

                    OutlinedTextField(
                        value = vehicleName,
                        onValueChange = {
                            vehicleName = it
                        },
                        label = {
                            Text("Vehicle Name")
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == VehicleType.CAR,
                            onClick = {
                                selectedType = VehicleType.CAR
                            }
                        )
                        Text("Car")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedType == VehicleType.BIKE,
                            onClick = {
                                selectedType = VehicleType.BIKE
                            }
                        )
                        Text("Bike")
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (vehicleName.isNotBlank()) {

                            onAddVehicle(
                                vehicleName,
                                selectedType
                            )

                            vehicleName = ""
                            selectedType = VehicleType.CAR
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
                        showDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Text("+")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                Text(
                    text = "My Vehicles",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            items(vehicles) { vehicle ->

                VehicleCard(
                    name = vehicle.name,
                    type = vehicle.type.name,
                    onClick = {
                        onVehicleClick(vehicle.id)
                    }
                )
            }
        }
    }
}