package com.example.mileagetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = FGPrimaryLight,
    onPrimary = FGOnPrimaryLight,

    secondary = FGSecondaryLight,
    onSecondary = FGOnSecondaryLight,

    background = FGBackgroundLight,
    onBackground = FGOnBackgroundLight,

    surface = FGSurfaceLight,
    onSurface = FGOnSurfaceLight,

    error = FGErrorLight,

    outline = FGDividerLight
)

private val DarkColorScheme = darkColorScheme(

    primary = FGPrimaryDark,
    onPrimary = FGOnPrimaryDark,

    secondary = FGSecondaryDark,
    onSecondary = FGOnSecondaryDark,

    background = FGBackgroundDark,
    onBackground = FGOnBackgroundDark,

    surface = FGSurfaceDark,
    onSurface = FGOnSurfaceDark,

    error = FGErrorDark,

    outline = FGDividerDark
)

@Composable
fun FuelGarageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) DarkColorScheme
        else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}