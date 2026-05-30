package com.example.mileagetracker.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import com.example.mileagetracker.data.entity.FuelEntry
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit
import com.example.mileagetracker.utils.formatCurrency
import com.example.mileagetracker.utils.formatDistance
import com.example.mileagetracker.utils.formatFuel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExtendedFloatingActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit,
    onEditEntry: (FuelEntry) -> Unit,
    onAddFuel: () -> Unit
) {
    val entries by viewModel.entries.collectAsState()

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

    var entryToDelete by remember { mutableStateOf<FuelEntry?>(null) }

    if (entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { entryToDelete = null },
            title = { Text("Delete Fuel Entry?") },
            text = {
                Column {
                    Text(formatDate(entryToDelete!!.dateMillis))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Odometer: ${entryToDelete!!.odometerKm} km")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("This action cannot be undone.")
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEntry(entryToDelete!!.id)
                        entryToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = { entryToDelete = null }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Fuel History")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFuel
            ) {
                Text("+")
            }
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No fuel entries found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(entries) { index, entry ->
                    val entryNumber = entries.size - index
                    FuelEntryCard(
                        entryNumber = entryNumber,
                        entry = entry,
                        distanceUnit = distanceUnit,
                        fuelUnit = fuelUnit,
                        currency = currency,
                        onEditClick = { onEditEntry(entry) },
                        onDeleteClick = { entryToDelete = entry }
                    )
                }
            }
        }
    }
}

@Composable
private fun FuelEntryCard(
    entryNumber: Int,
    entry: FuelEntry,
    distanceUnit: DistanceUnit,
    fuelUnit: FuelUnit,
    currency: Currency,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Entry #$entryNumber",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = formatDate(entry.dateMillis),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box {

                    IconButton(
                        onClick = {
                            menuExpanded = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = {
                            menuExpanded = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Edit") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onEditClick()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                menuExpanded = false
                                onDeleteClick()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Odometer Reading: ${
                    formatDistance(
                        entry.odometerKm,
                        distanceUnit
                    )
                }"
            )
            Text(
                "Amount Paid: ${
                    formatCurrency(
                        entry.amountPaid,
                        currency
                    )
                }"
            )
            Text(
                "Fuel Price: ${
                    formatCurrency(
                        entry.fuelPrice,
                        currency
                    )
                }/${
                    if (fuelUnit == FuelUnit.GALLONS) "gal"
                    else "L"
                }"
            )
            Text(
                "Fuel Quantity: ${
                    formatFuel(
                        entry.fuelQuantity,
                        fuelUnit
                    )
                }"
            )

            if (entry.fullTank) {
                Spacer(modifier = Modifier.height(6.dp))
                AssistChip(
                    onClick = {},
                    label = { Text("FULL TANK") }
                )
            }
        }
    }
}

private fun formatDate(
    dateMillis: Long
): String {
    return SimpleDateFormat("dd-MMM-yy", Locale.getDefault()).format(Date(dateMillis))
}