package com.example.mileagetracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle

object FGTypography {

    val ScreenTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.headlineMedium

    val SectionTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge

    val CardTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleMedium

    val Body: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    val Caption: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    val Label: TextStyle
        @Composable
        get() = MaterialTheme.typography.labelLarge
}