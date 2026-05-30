package com.example.mileagetracker.ui.fuel.validation

enum class ValidationType {
    VALID,
    SOFT_WARNING,
    HARD_BLOCK
}

data class ChronologyValidationResult(
    val type: ValidationType,
    val conflictEntryId: Long? = null,
    val conflictEntryNumber: Int? = null,
    val conflictDateMillis: Long? = null,
    val conflictOdometerKm: Double? = null,
    val message: String = ""
)