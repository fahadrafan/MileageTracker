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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SelectableDates
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelEntryScreen(
    viewModel: FuelEntryViewModel,
    vehicleId: Long,
    isEditMode: Boolean = false,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(vehicleId) {
        if (!isEditMode) {
            viewModel.loadVehicleDefaults(vehicleId)
        }
    }
    LaunchedEffect(uiState.saveSuccessful) {
        if (uiState.saveSuccessful) {
            onBack()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val selectedDate =
                        Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    return !selectedDate.isAfter(LocalDate.now())
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.setDateMillis(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (!isEditMode) "Add Fuel"
                        else "Edit Fuel Entry"
                    )
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
                value = uiState.refillDateText,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { viewModel.updateDateText(it) },
                isError = uiState.dateError != null,
                label = { Text("Refill Date") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!it.isFocused) {
                            viewModel.onDateFocusLost()
                        }
                    }
            )
            uiState.dateError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.odometer,
                onValueChange = { viewModel.updateOdometer(it) },
                isError = uiState.odometerError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("Odometer Reading") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (!it.isFocused) {
                            viewModel.onOdometerFocusLost()
                        }
                    }

            )
            uiState.odometerError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (uiState.lastOdometer.isNotBlank()) {
                Text(
                    text = "Last Reading: ${uiState.lastOdometer} km",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.amountPaid,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                onValueChange = {                    viewModel.updateAmountPaid(it)                },
                isError = uiState.amountPaidError != null,
                label = { Text("Amount Paid (₹)") },
                modifier = Modifier.fillMaxWidth()
            )
            uiState.amountPaidError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = uiState.fuelPrice,
                onValueChange = {                    viewModel.updateFuelPrice(it)                },
                isError = uiState.fuelPriceError != null,
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
            uiState.fuelPriceError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        if (isEditMode) "Discard"
                        else "Cancel"
                    )
                }
                Button(
                    onClick = {
                        viewModel.saveFuel(vehicleId)
                    },
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        if (isEditMode) "Update"
                        else "Save"
                    )
                }
            }
        }
    }
}