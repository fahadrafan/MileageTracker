package com.example.mileagetracker.data.preferences.model

enum class Currency(
    val symbol: String,
    val displayName: String
) {

    INR("₹", "Indian Rupee"),
    USD("$", "US Dollar"),
    EUR("€", "Euro"),
    GBP("£", "British Pound"),
    JPY("¥", "Japanese Yen"),
    CNY("¥", "Chinese Yuan"),
    AUD("A$", "Australian Dollar"),
    CAD("C$", "Canadian Dollar"),
    SGD("S$", "Singapore Dollar"),
    AED("د.إ", "UAE Dirham"),
    CHF("CHF", "Swiss Franc")
}