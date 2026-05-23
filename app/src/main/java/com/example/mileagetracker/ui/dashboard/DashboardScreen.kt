package com.example.mileagetracker.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import com.example.mileagetracker.data.entity.VehicleType

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddFuelClick: () -> Unit,
    onHistoryClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showVehicleSelector by remember {
        mutableStateOf(false)
    }

    var vehicleName by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf(VehicleType.CAR)
    }

    if (showVehicleSelector) {

        AlertDialog(
            onDismissRequest = {
                showVehicleSelector = false
            },

            title = {
                Text("Select Vehicle")
            },

            text = {

                Column {

                    uiState.vehicles.forEach { vehicle ->

                        TextButton(
                            onClick = {

                                viewModel.selectVehicle(
                                    vehicle
                                )

                                showVehicleSelector =
                                    false
                            }
                        ) {

                            val icon =
                                if (
                                    vehicle.type.name == "CAR"
                                ) "🚗"
                                else "🏍"

                            Text(
                                "$icon ${vehicle.name}"
                            )
                        }
                    }
                }
            },

            confirmButton = {}
        )
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
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected =
                                selectedType ==
                                        VehicleType.CAR,
                            onClick = {
                                selectedType =
                                    VehicleType.CAR
                            }
                        )

                        Text("Car")
                    }

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected =
                                selectedType ==
                                        VehicleType.BIKE,
                            onClick = {
                                selectedType =
                                    VehicleType.BIKE
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

                            viewModel.addVehicle(
                                vehicleName,
                                selectedType
                            )

                            vehicleName = ""

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
                onClick = onAddFuelClick
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Vehicle")
                }
            }

            item {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        TextButton(
                            onClick = {
                                showVehicleSelector = true
                            }
                        ) {

                            val vehicle =
                                uiState.selectedVehicle

                            val icon =
                                if (
                                    vehicle?.type?.name == "CAR"
                                ) "🚗"
                                else "🏍"

                            Text(
                                "$icon ${vehicle?.name ?: "No Vehicle"} ▼"
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Current Mileage",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "18.6 km/l",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
            }

            item {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    StatCard(
                        title = "Average",
                        value = "17.9 km/l",
                        modifier = Modifier.weight(1f)
                    )

                    StatCard(
                        title = "Cost/km",
                        value = "₹5.66",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {

                Button(
                    onClick = onHistoryClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View History")
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(title)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}