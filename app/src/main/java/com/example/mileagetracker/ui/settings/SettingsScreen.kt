package com.example.mileagetracker.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit
import com.example.mileagetracker.data.preferences.model.ThemeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var pendingImportUri by remember { mutableStateOf<Uri?>(null) }
    var pendingExport by remember { mutableStateOf<PendingExport?>(null) }
    var exportedFile by remember { mutableStateOf<ExportedFile?>(null) }
    var exportError by remember { mutableStateOf<String?>(null) }
    var importResult by remember { mutableStateOf<ImportResult?>(null) }
    var showExportScreen by remember { mutableStateOf(false) }
    var exportFormat by remember { mutableStateOf<ExportFormat?>(null) }
    var showImportInfoDialog by remember { mutableStateOf(false) }

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun shareFile(uri: Uri, mimeType: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(intent, "Share export")
        )
    }

    fun readText(uri: Uri): String? {
        return context.contentResolver.openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }
    }

    fun writeText(uri: Uri, text: String) {
        context.contentResolver.openOutputStream(uri)
            ?.bufferedWriter()
            ?.use { it.write(text) }
    }

    val jsonExportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        val export = pendingExport
        pendingExport = null

        if (uri == null || export == null) return@rememberLauncherForActivityResult

        scope.launch {
            runCatching {
                writeText(uri, export.content)
            }.onSuccess {
                exportedFile = ExportedFile(
                    title = export.successTitle,
                    message = export.successMessage,
                    uri = uri,
                    mimeType = export.mimeType
                )
            }.onFailure {
                exportError = it.message ?: "Export failed. Please try again."
            }
        }
    }

    val csvExportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        val export = pendingExport
        pendingExport = null

        if (uri == null || export == null) return@rememberLauncherForActivityResult

        scope.launch {
            runCatching {
                writeText(uri, export.content)
            }.onSuccess {
                exportedFile = ExportedFile(
                    title = export.successTitle,
                    message = export.successMessage,
                    uri = uri,
                    mimeType = export.mimeType
                )
            }.onFailure {
                exportError = it.message ?: "CSV export failed. Please try again."
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        pendingImportUri = uri
    }

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

    BackHandler(enabled = showExportScreen) {
        showExportScreen = false
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },

        topBar = {

            TopAppBar(

                title = {
                    Text(
                        if (showExportScreen)
                            "Export Backup"
                        else
                            "Settings"
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            if (showExportScreen)
                                showExportScreen = false
                            else
                                onBackClick()
                        }
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

            if (showExportScreen) {

                item {

                    SettingsSection(
                        title = "Choose Format"
                    ) {

                        SettingsRow(
                            title = "Export as Backup JSON",
                            value = "For restoring app data later",
                            showChevron = false,
                            onClick = {
                                exportFormat = ExportFormat.JSON
                            }
                        )

                        HorizontalDivider()

                        SettingsRow(
                            title = "Export as CSV File",
                            value = "For spreadsheets and analysis",
                            showChevron = false,
                            onClick = {
                                exportFormat = ExportFormat.CSV
                            }
                        )
                    }
                }
            } else {

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

                item {

                    SettingsSection(
                        title = "Backup"
                    ) {

                        SettingsRow(
                            title = "Export Backup",
                            value = "Choose JSON or CSV",
                            onClick = {
                                showExportScreen = true
                            }
                        )

                        HorizontalDivider()

                        SettingsRow(
                            title = "Import Backup",
                            value = "Choose backup JSON file",
                            onClick = {
                                showImportInfoDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    exportedFile?.let { file ->

        AlertDialog(
            onDismissRequest = {
                exportedFile = null
            },
            title = {
                Text(file.title)
            },
            text = {
                Text(file.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exportedFile = null
                        shareFile(file.uri, file.mimeType)
                    }
                ) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        exportedFile = null
                    }
                ) {
                    Text("Not Now")
                }
            }
        )
    }

    exportError?.let { message ->

        AlertDialog(
            onDismissRequest = {
                exportError = null
            },
            title = {
                Text("Export Failed")
            },
            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exportError = null
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    importResult?.let { result ->

        AlertDialog(
            onDismissRequest = {
                importResult = null
            },
            title = {
                Text(result.title)
            },
            text = {
                Text(result.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        importResult = null
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    exportFormat?.let { format ->

        AlertDialog(
            onDismissRequest = {
                exportFormat = null
            },
            title = {
                Text(format.title)
            },
            text = {
                Text(format.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        exportFormat = null

                        scope.launch {
                            runCatching {
                                when (format) {
                                    ExportFormat.JSON ->
                                        PendingExport(
                                            content = viewModel.exportBackupJson(),
                                            successTitle = "Backup Exported",
                                            successMessage = "Your backup JSON file has been saved. Do you want to share it now?",
                                            mimeType = "application/json"
                                        )

                                    ExportFormat.CSV ->
                                        PendingExport(
                                            content = viewModel.exportBackupCsv(),
                                            successTitle = "CSV Exported",
                                            successMessage = "Your CSV file has been saved. Do you want to share it now?",
                                            mimeType = "text/csv"
                                        )
                                }
                            }.onSuccess { export ->
                                pendingExport = export

                                when (format) {
                                    ExportFormat.JSON ->
                                        jsonExportLauncher.launch(
                                            "fuel_garage_backup.json"
                                        )

                                    ExportFormat.CSV ->
                                        csvExportLauncher.launch(
                                            "fuel_garage_data.csv"
                                        )
                                }
                            }.onFailure {
                                exportError = it.message
                                    ?: "Export failed. Please try again."
                            }
                        }
                    }
                ) {
                    Text("Export")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        exportFormat = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showImportInfoDialog) {

        AlertDialog(
            onDismissRequest = {
                showImportInfoDialog = false
            },
            title = {
                Text("Import Backup JSON")
            },
            text = {
                Text("Only backup JSON files exported from Fuel Garage can be imported. CSV files are for viewing only and cannot restore app data.\n\nNote: Any existing vehicle data will be erased before importing.\n\nDo you want to continue?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImportInfoDialog = false
                        importLauncher.launch(
                            arrayOf("application/json", "text/*")
                        )
                    }
                ) {
                    Text("Yes, Choose File")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImportInfoDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    pendingImportUri?.let { uri ->

        AlertDialog(
            onDismissRequest = {
                pendingImportUri = null
            },
            title = {
                Text("Import Backup?")
            },
            text = {
                Text("This will replace your current vehicles, fuel entries, and settings.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingImportUri = null

                        scope.launch {
                            runCatching {
                                val text = readText(uri)
                                    ?: error("The selected file could not be opened. Please choose another backup JSON file.")
                                viewModel.restoreBackupJson(text)
                            }.onSuccess {
                                importResult = ImportResult(
                                    title = "Success ✅",
                                    message = "Your vehicles, fuel entries, and settings were restored successfully."
                                )
                            }.onFailure { error ->
                                importResult = ImportResult(
                                    title = "Import Failed ❌",
                                    message = error.message
                                        ?: "Restore failed. Your existing data was not changed."
                                )
                            }
                        }
                    }
                ) {
                    Text("Import")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        pendingImportUri = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
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
    showChevron: Boolean = true,
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

        if (showChevron) {
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

private enum class ExportFormat(
    val title: String,
    val message: String
) {
    JSON(
        title = "Export Backup JSON?",
        message = "This will export all vehicles, fuel entries, and settings into a backup JSON file. You can use this file later to restore your app data."
    ),
    CSV(
        title = "Export CSV File?",
        message = "This will export your vehicle and fuel entry data as a spreadsheet-friendly CSV file. CSV exports are for viewing or analysis only and cannot restore app data."
    )
}

private data class ExportedFile(
    val title: String,
    val message: String,
    val uri: Uri,
    val mimeType: String
)

private data class PendingExport(
    val content: String,
    val successTitle: String,
    val successMessage: String,
    val mimeType: String
)

private data class ImportResult(
    val title: String,
    val message: String
)

@Composable
fun SelectionDialog(
    title: String,
    options: List<String>,
    selectedOption: String,
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var visibleSelection by remember(selectedOption) {
        mutableStateOf(selectedOption)
    }

    fun selectOption(option: String) {
        visibleSelection = option

        scope.launch {
            delay(250)
            onOptionSelected(option)
        }
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(title)
        },

        text = {

            Column {

                options.forEachIndexed { index, option ->

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                selectOption(option)
                            }
                            .padding(vertical = 12.dp),

                        verticalAlignment =
                            Alignment.CenterVertically

                    ) {

                        Text(
                            text = option,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        if (option == visibleSelection) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (index < options.lastIndex) {
                        HorizontalDivider()
                    }
                }
            }
        },

        confirmButton = {}
    )
}
