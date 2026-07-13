package com.example.mileagetracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(

    displaySmall = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),

    titleMedium = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    labelLarge = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),

    labelMedium = TextStyle(
        fontFamily = FuelGarageFont,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)