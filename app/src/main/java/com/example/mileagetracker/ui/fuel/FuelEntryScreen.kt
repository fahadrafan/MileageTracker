package com.example.mileagetracker.ui.fuel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit
import com.example.mileagetracker.ui.components.fields.FGOutlinedTextField
import com.example.mileagetracker.ui.components.screen.FGScreen
import com.example.mileagetracker.ui.components.topbar.FGTopBar
import com.example.mileagetracker.ui.components.section.FGSectionHeader
import com.example.mileagetracker.ui.components.dialogs.FGDialog
import com.example.mileagetracker.ui.components.buttons.FGPrimaryButton
import com.example.mileagetracker.ui.components.buttons.FGSecondaryButton
import com.example.mileagetracker.ui.components.fields.FGSwitch
import com.example.mileagetracker.ui.theme.tokens.FGSpacing

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

    val context = LocalContext.current

    val preferencesRepository = remember {
        UserPreferencesRepository(context)
    }

    val distanceUnit by preferencesRepository
        .distanceUnit
        .collectAsStateWithLifecycle(
            initialValue = DistanceUnit.KM
        )

    val fuelUnit by preferencesRepository
        .fuelUnit
        .collectAsStateWithLifecycle(
            initialValue = FuelUnit.LITRES
        )

    val currency by preferencesRepository
        .currency
        .collectAsStateWithLifecycle(
            initialValue = Currency.INR
        )

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

    if (uiState.showSoftWarningDialog) {

        AlertDialog(
            onDismissRequest = {
                viewModel.dismissSoftWarningDialog()
            },

            title = {
                Text("Save Entry?")
            },

            text = {
                Text(
                    "This entry will be inserted between existing records.\n\nSave anyway?"
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.confirmSoftWarningSave(vehicleId)
                    }
                ) {
                    Text("Save Anyway")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissSoftWarningDialog()
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    uiState.chronologyError?.let { error ->
        FGDialog(
            onDismissRequest = { },
            title = "Cannot Save",
            message = error,
            confirmText = "OK",
            onConfirm = {
                viewModel.clearChronologyError()
            }
        )
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
            FGTopBar(
                title = if (!isEditMode) "Add Fuel" else "Edit Fuel Entry",
                onBackClick = onBack
            )
        }
    ) { padding ->

        FGScreen(
            contentPadding = padding
        ) {

            FGSectionHeader(
                title = "Refuel Details"
            )
            FGOutlinedTextField(
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

            FGOutlinedTextField(
                value = uiState.odometer,
                onValueChange = { viewModel.updateOdometer(it) },
                isError = uiState.odometerError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = {
                    Text(
                        "Odometer Reading (${
                            if (distanceUnit == DistanceUnit.MILES) "mi"
                            else "km"
                        })"
                    )
                },
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
                    text =
                        "Last Reading: ${uiState.lastOdometer} ${
                            if (distanceUnit == DistanceUnit.MILES) "mi" else "km"
                        }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            FGSectionHeader(
                title = "Fuel Details",
                subtitle = "Enter any two values to calculate the third."
            )

            FGOutlinedTextField(
                value = uiState.amountPaid,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                onValueChange = { viewModel.updateAmountPaid(it) },
                isError = uiState.amountPaidError != null,
                label = { Text("Amount Paid (${currency.symbol})") },
                modifier = Modifier.fillMaxWidth()
            )
            uiState.amountPaidError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            FGOutlinedTextField(
                value = uiState.fuelPrice,
                onValueChange = { viewModel.updateFuelPrice(it) },
                isError = uiState.fuelPriceError != null,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),
                label = {
                    Text(
                        "Fuel Price (${currency.symbol}/${
                            if (fuelUnit == FuelUnit.GALLONS) "gal"
                            else "L"
                        })"
                    )
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

            FGOutlinedTextField(
                value = uiState.fuelQuantity,
                onValueChange = { viewModel.updateLitres(it) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                label = {
                    Text(
                        "Fuel Quantity (${
                            if (fuelUnit == FuelUnit.GALLONS) "Gallons"
                            else "Litres"
                        })"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.clickable {
                    viewModel.updateFullTank(!uiState.fullTank)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {

                FGSwitch(
                    checked = uiState.fullTank,
                    onCheckedChange = {
                        viewModel.updateFullTank(it)
                    }
                )

                Spacer(modifier = Modifier.width(FGSpacing.MD))

                Text(
                    text = "Full Tank",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FGSecondaryButton(
                    text = if (isEditMode) "Discard" else "Cancel",
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                )

                FGPrimaryButton(
                    text = if (isEditMode) "Update" else "Save",
                    onClick = {
                        viewModel.saveFuel(vehicleId)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}