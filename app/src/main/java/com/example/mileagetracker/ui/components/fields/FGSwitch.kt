package com.example.mileagetracker.ui.components.fields

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable

@Composable
fun FGSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = SwitchDefaults.colors(

            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
            checkedTrackColor = MaterialTheme.colorScheme.primary,

            uncheckedThumbColor = MaterialTheme.colorScheme.surface,
            uncheckedTrackColor = MaterialTheme.colorScheme.outline,

            disabledCheckedThumbColor =
                MaterialTheme.colorScheme.surface,

            disabledCheckedTrackColor =
                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),

            disabledUncheckedThumbColor =
                MaterialTheme.colorScheme.surface,

            disabledUncheckedTrackColor =
                MaterialTheme.colorScheme.outline.copy(alpha = 0.40f)
        )
    )
}