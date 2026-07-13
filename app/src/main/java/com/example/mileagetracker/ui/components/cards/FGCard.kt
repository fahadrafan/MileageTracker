package com.example.mileagetracker.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mileagetracker.ui.theme.tokens.FGCardDefaults

@Composable
fun FGCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            FGCardDefaults.BorderWidth,
            MaterialTheme.colorScheme.outline
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = FGCardDefaults.Elevation
        ),
        content = content
    )
}