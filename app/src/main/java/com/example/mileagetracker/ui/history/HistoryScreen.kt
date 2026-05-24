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
import com.example.mileagetracker.data.entity.FuelEntry
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBack: () -> Unit
) {

    val entries by viewModel.entries.collectAsState()
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
                items(entries) { entry ->
                    FuelEntryCard(
                        entry = entry,
                        onDeleteClick = { entryToDelete = entry }
                    )
                }
            }
        }
    }
}

@Composable
private fun FuelEntryCard(
    entry: FuelEntry,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(entry.dateMillis),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = onDeleteClick
                ) {
                    Text("Delete")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Amount Paid: ₹${entry.amountPaid}")
            Text("Fuel Price: ₹%.2f/L".format(entry.fuelPrice))
            Text("Litres: %.2f L".format(entry.litres))
            Text("Odometer: ${entry.odometerKm} km")

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