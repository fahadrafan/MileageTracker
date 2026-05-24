package com.example.mileagetracker.utils

import com.example.mileagetracker.data.entity.FuelEntry
import com.example.mileagetracker.utils.VehicleStatistics

data class VerifiedMileageResult(
    val startOdometer: Double,
    val endOdometer: Double,
    val distanceKm: Double,
    val fuelUsedLitres: Double,
    val mileageKmPerLitre: Double
)


object MileageCalculator {

    /**
     * Estimated mileage using previous entry.
     *
     * distance travelled ÷ current refill litres
     */
    fun calculateEstimatedMileage(
        previousEntry: FuelEntry,
        currentEntry: FuelEntry
    ): Double {
        val distance = currentEntry.odometerKm - previousEntry.odometerKm
        if (distance <= 0) {
            return 0.0
        }
        if (currentEntry.litres <= 0) {
            return 0.0
        }
        return distance / currentEntry.litres
    }

    /**
     * Verified mileage between two full tank entries.
     *
     * Example:
     *
     * Full Tank
     *     ↓
     * Partial
     *     ↓
     * Partial
     *     ↓
     * Full Tank
     *
     * distance travelled ÷ fuel consumed
     */
    fun calculateVerifiedMileage(
        startFullTank: FuelEntry,
        endFullTank: FuelEntry,
        intermediateEntries: List<FuelEntry>
    ): Double {

        val distance = endFullTank.odometerKm - startFullTank.odometerKm
        if (distance <= 0) {
            return 0.0
        }
        val totalFuelUsed = intermediateEntries.sumOf { it.litres } + endFullTank.litres
        if (totalFuelUsed <= 0) {
            return 0.0
        }
        return distance / totalFuelUsed
    }

    fun getVerifiedMileageRecords(
        entries: List<FuelEntry>
    ): List<VerifiedMileageResult> {

        if (entries.size < 2) {
            return emptyList()
        }

        val sortedEntries = entries.sortedBy { it.odometerKm }
        val results = mutableListOf<VerifiedMileageResult>()
        var startFullTank: FuelEntry? = null
        val intermediateEntries = mutableListOf<FuelEntry>()

        for (entry in sortedEntries) {
            if (entry.fullTank) {
                if (startFullTank == null) {
                    startFullTank = entry
                } else {
                    val mileage =
                        calculateVerifiedMileage(startFullTank, entry, intermediateEntries)
                    val fuelUsed = intermediateEntries.sumOf {
                        it.litres
                    } + entry.litres

                    results.add(
                        VerifiedMileageResult(
                            startOdometer = startFullTank.odometerKm,
                            endOdometer = entry.odometerKm,
                            distanceKm = entry.odometerKm - startFullTank.odometerKm,
                            fuelUsedLitres = fuelUsed,
                            mileageKmPerLitre = mileage
                        )
                    )
                    startFullTank = entry
                    intermediateEntries.clear()
                }
            } else {
                if (startFullTank != null) {
                    intermediateEntries.add(entry)
                }
            }
        }
        return results
    }

    fun calculateAverageVerifiedMileage(
        entries: List<FuelEntry>
    ): Double {
        val records = getVerifiedMileageRecords(entries)
        if (records.isEmpty()) {
            return 0.0
        }
        return records.map { it.mileageKmPerLitre }.average()
    }

    fun calculateStatistics(entries: List<FuelEntry>): VehicleStatistics {

        if (entries.isEmpty()) {
            return VehicleStatistics()
        }

        val sortedEntries = entries.sortedBy { it.odometerKm }
        val totalSpent = entries.sumOf { it.amountPaid }
        val fuelConsumed = entries.sumOf { it.litres }
        val totalDistance =
            if (sortedEntries.size >= 2) {
                maxOf(
                    0.0,
                    sortedEntries.last().odometerKm - sortedEntries.first().odometerKm
                )
            } else {
                0.0
            }

        val estimatedMileage =
            if (sortedEntries.size >= 2) {
                calculateEstimatedMileage(
                    sortedEntries[
                        sortedEntries.lastIndex - 1
                    ],
                    sortedEntries.last()
                )

            } else {
                0.0
            }

        val verifiedRecords =
            getVerifiedMileageRecords(entries)

        val lastVerifiedMileage =
            verifiedRecords
                .lastOrNull()
                ?.mileageKmPerLitre
                ?: 0.0

        val averageVerifiedMileage =
            calculateAverageVerifiedMileage(entries)

        val costPerKm =
            if (totalDistance > 0) {
                totalSpent / totalDistance
            } else {
                0.0
            }

        return VehicleStatistics(
            estimatedMileage = estimatedMileage,
            lastVerifiedMileage = lastVerifiedMileage,
            averageVerifiedMileage = averageVerifiedMileage,
            totalDistance = totalDistance,
            fuelConsumed = fuelConsumed,
            totalSpent = totalSpent,
            costPerKm = costPerKm
        )
    }
}