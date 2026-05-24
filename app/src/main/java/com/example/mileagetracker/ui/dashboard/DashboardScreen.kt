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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.mileagetracker.data.entity.VehicleType

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddFuelClick: () -> Unit,
    onHistoryClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val stats = uiState.statistics

    var showDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
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
            onDismissRequest = { showVehicleSelector = false },
            title = { Text("Select Vehicle") },
            text = {
                Column {
                    uiState.vehicles.forEach { vehicle ->
                        TextButton(
                            onClick = {
                                viewModel.selectVehicle(vehicle)
                                showVehicleSelector = false
                            }
                        ) {
                            val icon =
                                if (vehicle.type.name == "CAR") "🚗"
                                else "🏍"
                            Text("$icon ${vehicle.name}")
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    if (showDialog) {

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Vehicle") },
            text = {
                Column {
                    OutlinedTextField(
                        value = vehicleName,
                        onValueChange = { vehicleName = it },
                        label = { Text("Vehicle Name") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == VehicleType.CAR,
                            onClick = { selectedType = VehicleType.CAR }
                        )
                        Text("Car")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == VehicleType.BIKE,
                            onClick = { selectedType = VehicleType.BIKE }
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

    if (showDeleteDialog) {

        val vehicle = uiState.selectedVehicle

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Vehicle?")
            },
            text = {
                Column {
                    Text("Vehicle: ${vehicle?.name ?: ""}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("This will permanently delete:")
                    Text("• Vehicle")
                    Text("• Fuel history")
                    Text("• Mileage statistics")
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        vehicle?.let { viewModel.deleteVehicle(it.id) }
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically
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

                            Spacer(
                                modifier = Modifier.weight(1f)
                            )

                            TextButton(
                                onClick = {
                                    showDeleteDialog = true
                                }
                            ) {
                                Text("Delete")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Current Mileage",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "%.1f km/l".format(stats.estimatedMileage),
                            style = MaterialTheme.typography.displaySmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Last Refuel Date: ${formatDate(stats.lastRefuelDate)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Average Mileage",
                        value = "%.1f km/l".format(stats.averageVerifiedMileage),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Cost/km",
                        value = "₹%.2f".format(stats.costPerKm),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Distance Covered",
                        value = "%.0f km".format(stats.totalDistance),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Total Fuel",
                        value = "%.1f L".format(stats.fuelConsumed),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Total Spent",
                        value = "₹%.0f".format(stats.totalSpent),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Last Verified",
                        value = "%.1f km/l".format(stats.lastVerifiedMileage),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Recent Fills",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = onHistoryClick) {
                                Text("View All")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        if (uiState.recentEntries.isEmpty()) {
                            Text("No fuel entries yet")
                        } else {
                            uiState.recentEntries.forEach { entry ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = formatDate(entry.dateMillis),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "₹%.0f".format(entry.amountPaid),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "%.1f L".format(entry.litres)
                                    )
                                }
                                if (entry != uiState.recentEntries.last()) {
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
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

private fun formatDate(dateMillis: Long): String {
    if (dateMillis == 0L) return "-"

    return SimpleDateFormat(
        "dd-MMM-yy",
        Locale.getDefault()
    ).format(Date(dateMillis))
}