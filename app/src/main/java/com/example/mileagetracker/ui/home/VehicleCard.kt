package com.example.mileagetracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mileagetracker.data.preferences.UserPreferencesRepository
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.utils.formatMileage

@Composable
fun VehicleCard(
    name: String,
    registrationNumber: String,
    fuelType: String,
    type: String,
    mileage: Double?,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current

    val preferencesRepository = remember {
        UserPreferencesRepository(context)
    }

    val distanceUnit by preferencesRepository
        .distanceUnit
        .collectAsStateWithLifecycle(
            initialValue = DistanceUnit.KM
        )

    var showMenu by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = if (type == "CAR") "🚗" else "🏍",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (registrationNumber.isNotBlank()) {
                        Text(
                            text = registrationNumber,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "$fuelType • ${
                            type.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        }",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Box {
                    IconButton(
                        onClick = {
                            showMenu = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Vehicle Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Edit")
                            },
                            onClick = {
                                showMenu = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Delete")
                            },
                            onClick = {
                                showMenu = false
                                onDelete()
                            }
                        )
                    }
                }

                Text(
                    text = mileage?.let {
                        if (it > 0) formatMileage(it, distanceUnit)
                        else "--"
                    } ?: "--",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}