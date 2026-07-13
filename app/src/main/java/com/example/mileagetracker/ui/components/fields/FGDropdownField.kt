package com.example.mileagetracker.ui.components.fields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FGDropdownField(
    value: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    content: @Composable ExposedDropdownMenuBoxScope.() -> Unit
) {

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            onExpandedChange(!expanded)
        }
    ) {

        FGOutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth(),
            singleLine = true,
            label = label,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        content()
    }
}