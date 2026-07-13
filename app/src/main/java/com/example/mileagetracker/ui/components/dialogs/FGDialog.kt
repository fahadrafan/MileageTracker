package com.example.mileagetracker.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun FGDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "OK",
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,

        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },

        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        },

        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(confirmText)
            }
        },

        dismissButton = {
            if (dismissText != null && onDismiss != null) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(dismissText)
                }
            }
        }
    )
}