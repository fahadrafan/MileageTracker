package com.example.mileagetracker.ui.home

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
import com.example.mileagetracker.ui.theme.FGTypography
import com.example.mileagetracker.ui.theme.tokens.FGSpacing
import androidx.compose.material3.CardDefaults
import com.example.mileagetracker.ui.theme.tokens.FGCardDefaults
import com.example.mileagetracker.ui.components.cards.FGCard

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
    FGCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FGCardDefaults.ContentPadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = if (type == "CAR") "🚗" else "🏍",
                    style = FGTypography.SectionTitle
                )
                Spacer(modifier = Modifier.width(FGSpacing.MD))
                Column {
                    Text(
                        text = name,
                        style = FGTypography.CardTitle
                    )
                    if (registrationNumber.isNotBlank()) {
                        Text(
                            text = registrationNumber,
                            style = FGTypography.Caption
                        )
                    }
                    Text(
                        text = "$fuelType • ${
                            type.lowercase()
                                .replaceFirstChar { it.uppercase() }
                        }",
                        style = FGTypography.Caption
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        if (mileage != null && mileage > 0) {

                            Text(
                                text = formatMileage(mileage, distanceUnit),
                                style = FGTypography.ScreenTitle
                            )

                            Text(
                                text = "Current Mileage",
                                style = FGTypography.Label,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        } else {

                            Text(
                                text = "No data",
                                style = FGTypography.CardTitle
                            )

                            Text(
                                text = "No fuel entries yet",
                                style = FGTypography.Label,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

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
                }

            }
        }
    }
}