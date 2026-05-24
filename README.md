# Fuel Garage 🚗⛽

A modern Android application for tracking vehicle fuel fills, calculating mileage, monitoring fuel expenses, and analyzing fuel efficiency trends over time.

Built with **Kotlin**, **Jetpack Compose**, **Room Database**, and **Material Design 3**.

---

## 📱 Project Overview

Fuel Garage helps vehicle owners:

- Track fuel fill-ups
- Calculate fuel efficiency automatically
- Monitor fuel expenses
- Compare mileage trends
- Manage multiple vehicles
- Maintain complete fuel history
- Analyze fuel consumption statistics

The application supports both **cars** and **motorcycles** and is designed to be lightweight, offline-first, and privacy-friendly.

---

# 🚀 Current Development Status

### MVP Progress: ~60%

### Completed

- ✅ Vehicle Management (Add Vehicle)
- ✅ Multi-Vehicle Support
- ✅ Default Vehicle Selection
- ✅ Fuel Entry Module
- ✅ Room Database Setup
- ✅ Repository Architecture
- ✅ Navigation Architecture
- ✅ Mileage Calculation Engine
- ✅ Dashboard Statistics
- ✅ Current Mileage Calculation
- ✅ Average Mileage Calculation
- ✅ Cost per Kilometer Calculation

### In Progress

- 🚧 Dashboard Refinement
- 🚧 Vehicle Edit/Delete Features
- 🚧 Data Validation Improvements

### Pending

- ❌ Fuel History Screen
- ❌ Statistics Screen
- ❌ Edit Fuel Entry
- ❌ Delete Fuel Entry
- ❌ Export Data
- ❌ Backup & Restore
- ❌ Settings Module

---

# 🎯 Product Vision

Create the most practical mileage and fuel tracking application for daily vehicle owners with:

- Simple fuel logging
- Accurate mileage tracking
- Useful analytics
- Clean UI
- Offline-first architecture
- No unnecessary complexity

---

# 📦 Technology Stack

## Language

- Kotlin

## UI

- Jetpack Compose
- Material Design 3

## Architecture

- MVVM
- Repository Pattern

## Database

- Room Database

## Dependency Injection (Future)

- Hilt

## Charts (Planned)

- MPAndroidChart
- Compose Charts

---

# 🗄 Database Structure

## Vehicle Entity

```kotlin
@Entity
data class Vehicle(
    val id: Long,
    val name: String,
    val registrationNumber: String,
    val vehicleType: String,
    val fuelType: String,
    val imageUri: String?,
    val isDefault: Boolean,
    val createdAt: Long
)
```

## Fuel Entry Entity

```kotlin
@Entity
data class FuelEntry(
    val id: Long,
    val vehicleId: Long,
    val date: Long,
    val odometer: Double,
    val quantity: Double,
    val totalCost: Double,
    val pricePerLiter: Double,
    val isFullTank: Boolean,
    val notes: String?
)
```

## Future Maintenance Entity

```kotlin
@Entity
data class MaintenanceEntry(
    val id: Long,
    val vehicleId: Long,
    val date: Long,
    val category: String,
    val amount: Double,
    val notes: String?
)
```

---

# 🧮 Mileage Calculation Logic

## First Fuel Entry

Mileage cannot be calculated.

Display:

```
—
```

---

## Subsequent Entries

Distance Travelled:

```text
Current Odometer - Previous Odometer
```

Mileage:

```text
Distance / Fuel Quantity
```

Mileage is calculated only when:

- Previous entry exists
- Current fill is marked as Full Tank

---

## Dashboard Metrics

### Current Mileage

Most recent valid mileage value.

### Average Mileage

Average of all calculated mileage entries.

### Cost per Kilometer

```text
Total Fuel Cost / Total Distance Travelled
```

### Total Distance

```text
Latest Odometer - First Odometer
```

### Total Fuel Used

Sum of all fuel quantities.

### Total Fuel Cost

Sum of all fuel expenses.

---

# 📋 Core Features

---

## 1. Vehicle Management

### Add Vehicle

Fields:

- Vehicle Name
- Registration Number
- Vehicle Type
- Fuel Type

Status:

✅ Completed

---

### Edit Vehicle

Planned Actions:

- Edit Details
- Change Default Vehicle

Status:

🚧 Planned

---

### Delete Vehicle

Requirements:

- Confirmation Dialog
- Remove related fuel history

Status:

🚧 Planned

---

## 2. Fuel Entry Module

### Current Fields

- Date
- Odometer Reading
- Fuel Quantity
- Total Cost
- Price Per Liter
- Full Tank Toggle
- Notes

Status:

✅ Implemented

### Planned Improvements

- Auto calculation of cost/liter
- Real-time validation
- Odometer consistency checks
- Better error handling

---

## 3. Vehicle Dashboard

### Main Mileage Card

Displays:

- Current Mileage
- Comparison with previous fill

Example:

```text
18.6 km/l
↑ 0.8 km/l from last fill
```

---

### Statistics Grid

Displays:

- Average Mileage
- Cost per KM
- Total Distance
- Fuel Used
- Total Spent
- Total Entries

---

### Recent Fills Section

Displays:

- Last 3 fuel entries
- Date
- Cost
- Mileage

Status:

🚧 In Progress

---

## 4. Fuel History

### Features

- Month-wise grouping
- Full fuel entry details
- Mileage display
- Distance travelled
- Full tank indicators

### Actions

- View Details
- Edit Entry
- Delete Entry

Status:

❌ Not Started

Priority:

HIGH

---

## 5. Statistics Screen

### Time Filters

- 7 Days
- 30 Days
- 3 Months
- 6 Months
- 1 Year
- All Time

---

### Summary Cards

- Average Mileage
- Best Mileage
- Worst Mileage
- Total Distance
- Fuel Used
- Total Cost

---

### Charts

Mileage Trend

Fuel Cost Trend

Cost Per KM Trend (Future)

Status:

❌ Not Started

Priority:

HIGH

---

## License

This project is currently under active development and not yet released publicly.

---

Built with ❤️ using Kotlin and Jetpack Compose.