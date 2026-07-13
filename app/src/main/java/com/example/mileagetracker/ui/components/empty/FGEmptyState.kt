package com.example.mileagetracker.ui.components.empty

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mileagetracker.ui.components.cards.FGCard
import com.example.mileagetracker.ui.theme.FGTypography
import com.example.mileagetracker.ui.theme.tokens.FGCardDefaults
import com.example.mileagetracker.ui.theme.tokens.FGSpacing

@Composable
fun FGEmptyState(
    emoji: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {

    FGCard(
        modifier = modifier.fillMaxWidth(),
        onClick = {}
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(FGCardDefaults.ContentPadding * 2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = emoji,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(FGSpacing.LG))

            Text(
                text = title,
                style = FGTypography.SectionTitle
            )

            Spacer(modifier = Modifier.height(FGSpacing.SM))

            Text(
                text = subtitle,
                style = FGTypography.Body
            )
        }
    }
}