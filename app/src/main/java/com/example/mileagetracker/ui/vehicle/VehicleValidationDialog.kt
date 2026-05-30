package com.example.mileagetracker.ui.vehicle

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun VehicleValidationDialog(
    error: String?,
    onDismiss: () -> Unit
) {
    error?.let {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Cannot Save Vehicle") },
            text = { Text(it) },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Edit")
                }
            }
        )
    }
}