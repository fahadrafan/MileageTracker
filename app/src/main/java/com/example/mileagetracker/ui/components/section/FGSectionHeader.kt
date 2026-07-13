package com.example.mileagetracker.ui.components.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mileagetracker.ui.theme.tokens.FGSpacing

@Composable
fun FGSectionHeader(
    title: String,
    subtitle: String? = null
) {
    Column {

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (!subtitle.isNullOrBlank()) {

            Spacer(Modifier.height(FGSpacing.XS))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}