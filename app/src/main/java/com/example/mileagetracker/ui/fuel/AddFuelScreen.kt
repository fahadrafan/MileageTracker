package com.example.mileagetracker.ui.fuel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.material3.Checkbox
import androidx.compose.ui.Alignment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFuelScreen(
    viewModel: AddFuelViewModel,
    vehicleId: Long,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(vehicleId) {

        viewModel.loadVehicleDefaults(
            vehicleId
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Fuel")
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = uiState.odometer,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),
                onValueChange = { viewModel.updateOdometer(it) },
                label = { Text("Odometer Reading") },
                modifier = Modifier.fillMaxWidth()
            )

            uiState.errorMessage?.let {

                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = uiState.amountPaid,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                onValueChange = {
                    viewModel.updateAmountPaid(it)
                },
                label = { Text("Amount Paid (₹)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.fuelPrice,
                onValueChange = {
                    viewModel.updateFuelPrice(it)
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                label = {
                    Text("Fuel Price (₹/L)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.litres,
                onValueChange = {},
                label = {
                    Text("Litres")
                },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = uiState.fullTank,
                    onCheckedChange = {
                        viewModel.updateFullTank(it)
                    }
                )

                TextButton(
                    onClick = {
                        viewModel.updateFullTank(
                            !uiState.fullTank
                        )
                    }
                ) {
                    Text("Full Tank")
                }
            }

            Button(
                onClick = {
                    viewModel.saveFuel(vehicleId)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}