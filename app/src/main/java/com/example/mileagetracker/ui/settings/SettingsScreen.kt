package com.example.mileagetracker.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit
import com.example.mileagetracker.data.preferences.model.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit
) {

    val themeMode by viewModel.themeMode.collectAsState()

    val distanceUnit by viewModel.distanceUnit.collectAsState()

    val fuelUnit by viewModel.fuelUnit.collectAsState()

    val currency by viewModel.currency.collectAsState()

    var showThemeDialog by remember {
        mutableStateOf(false)
    }

    var showDistanceDialog by remember {
        mutableStateOf(false)
    }

    var showFuelDialog by remember {
        mutableStateOf(false)
    }

    var showCurrencyDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Settings")
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(24.dp)

        ) {

            item {

                SettingsSection(
                    title = "Appearance"
                ) {

                    SettingsRow(
                        title = "Theme",
                        value = themeMode.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        onClick = {
                            showThemeDialog = true
                        }
                    )
                }
            }

            item {

                SettingsSection(
                    title = "Units"
                ) {

                    SettingsRow(
                        title = "Distance Unit",
                        value =
                            if (distanceUnit == DistanceUnit.KM)
                                "Kilometers"
                            else
                                "Miles",

                        onClick = {
                            showDistanceDialog = true
                        }
                    )

                    HorizontalDivider()

                    SettingsRow(
                        title = "Fuel Unit",
                        value =
                            if (fuelUnit == FuelUnit.LITRES)
                                "Litres"
                            else
                                "Gallons",

                        onClick = {
                            showFuelDialog = true
                        }
                    )
                }
            }

            item {

                SettingsSection(
                    title = "Regional"
                ) {

                    SettingsRow(
                        title = "Currency",
                        value =
                            "${currency.symbol} ${currency.name}",

                        onClick = {
                            showCurrencyDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showThemeDialog) {

        SelectionDialog(

            title = "Choose Theme",

            options = ThemeMode.entries.map {
                it.name.lowercase()
                    .replaceFirstChar { c -> c.uppercase() }
            },

            selectedOption =
                themeMode.name.lowercase()
                    .replaceFirstChar { it.uppercase() },

            onDismiss = {
                showThemeDialog = false
            },

            onOptionSelected = {

                viewModel.setThemeMode(
                    ThemeMode.valueOf(
                        it.uppercase()
                    )
                )

                showThemeDialog = false
            }
        )
    }

    if (showDistanceDialog) {

        SelectionDialog(

            title = "Distance Unit",

            options = listOf(
                "Kilometers",
                "Miles"
            ),

            selectedOption =
                if (distanceUnit == DistanceUnit.KM)
                    "Kilometers"
                else
                    "Miles",

            onDismiss = {
                showDistanceDialog = false
            },

            onOptionSelected = {

                viewModel.setDistanceUnit(

                    if (it == "Miles")
                        DistanceUnit.MILES
                    else
                        DistanceUnit.KM
                )

                showDistanceDialog = false
            }
        )
    }

    if (showFuelDialog) {

        SelectionDialog(

            title = "Fuel Unit",

            options = listOf(
                "Litres",
                "Gallons"
            ),

            selectedOption =
                if (fuelUnit == FuelUnit.LITRES)
                    "Litres"
                else
                    "Gallons",

            onDismiss = {
                showFuelDialog = false
            },

            onOptionSelected = {

                viewModel.setFuelUnit(

                    if (it == "Gallons")
                        FuelUnit.GALLONS
                    else
                        FuelUnit.LITRES
                )

                showFuelDialog = false
            }
        )
    }

    if (showCurrencyDialog) {

        SelectionDialog(

            title = "Currency",

            options = Currency.entries.map {
                "${it.symbol} ${it.name}"
            },

            selectedOption =
                "${currency.symbol} ${currency.name}",

            onDismiss = {
                showCurrencyDialog = false
            },

            onOptionSelected = { selected ->

                val selectedCurrency =
                    Currency.entries.first {

                        "${it.symbol} ${it.name}" == selected
                    }

                viewModel.setCurrency(
                    selectedCurrency
                )

                showCurrencyDialog = false
            }
        )
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                content()
            }
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    value: String,
    onClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(16.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween,

        verticalAlignment =
            Alignment.CenterVertically

    ) {

        Column {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null
        )
    }
}

@Composable
fun SelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(title)
        },

        text = {

            Column {

                options.forEach { option ->

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onOptionSelected(option)
                            }
                            .padding(vertical = 12.dp),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        RadioButton(
                            selected =
                                option == selectedOption,

                            onClick = {

                                onOptionSelected(option)
                            }
                        )

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Text(option)
                    }
                }
            }
        },

        confirmButton = {}
    )
}