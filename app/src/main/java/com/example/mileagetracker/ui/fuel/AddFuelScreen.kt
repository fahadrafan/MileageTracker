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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFuelScreen(
    viewModel: AddFuelViewModel,
    vehicleId: Long,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

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
                onValueChange = { viewModel.updateOdometer(it) },
                label = { Text("Odometer Reading") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.litres,
                onValueChange = { viewModel.updateLitres(it) },
                label = { Text("Litres") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.amountPaid,
                onValueChange = {
                    viewModel.updateAmountPaid(it)
                },
                label = { Text("Amount Paid (₹)") },
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

                Text("Full Tank")
            }

            Button(
                onClick = {
                    viewModel.saveFuel(vehicleId)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}