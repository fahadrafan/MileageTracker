package com.example.mileagetracker.utils

import com.example.mileagetracker.data.preferences.model.Currency
import com.example.mileagetracker.data.preferences.model.DistanceUnit
import com.example.mileagetracker.data.preferences.model.FuelUnit

fun formatDistance(
    value: Double,
    unit: DistanceUnit
): String {

    return if (unit == DistanceUnit.MILES) {
        "${String.format("%.1f", value)} mi"
    } else {
        "${String.format("%.1f", value)} km"
    }
}

fun formatFuel(
    value: Double,
    unit: FuelUnit
): String {

    return if (unit == FuelUnit.GALLONS) {
        "${String.format("%.2f", value)} gal"
    } else {
        "${String.format("%.2f", value)} L"
    }
}

fun formatCurrency(
    value: Double,
    currency: Currency
): String {

    return "${currency.symbol}${String.format("%.2f", value)}"
}

fun formatMileage(
    value: Double,
    distanceUnit: DistanceUnit
): String {

    return if (distanceUnit == DistanceUnit.MILES) {
        "${String.format("%.1f", value)} mpg"
    } else {
        "${String.format("%.1f", value)} km/l"
    }
}