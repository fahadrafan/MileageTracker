package com.example.mileagetracker.ui.components.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mileagetracker.ui.theme.tokens.FGScreenDefaults

@Composable
fun FGScreen(
    contentPadding: PaddingValues,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(FGScreenDefaults.ContentPadding),
        verticalArrangement = Arrangement.spacedBy(
            FGScreenDefaults.SectionSpacing
        )
    ) {
        content()
    }
}