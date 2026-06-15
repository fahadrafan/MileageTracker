# 🛣 Fuel Garage Development Roadmap

---

## Current Status

Fuel Garage now has a strong and polished MVP foundation:

* Vehicle add, edit, delete
* Fuel entry add, edit, delete
* Dynamic fuel history with entry numbers
* Monthly grouped fuel history
* Dashboard statistics and recent fills
* Vehicle and fuel-entry validation
* Chronology validation with hard blocks
* Soft warnings for inserted entries
* Intelligent three-way fuel calculator
* Previous-value defaults and recalculation
* Settings for theme, units, and currency
* Local backup and restore using JSON
* CSV export for spreadsheet-friendly data
* Android Storage Access Framework export/import flow
* Post-export sharing through Android share sheet
* Polished empty states for vehicles and fuel entries
* Dark theme support
* Device-tested backup and export flows

The app remains offline-first and does not require accounts, Firebase, Google Drive SDK, or cloud sign-in.

---

## Phase 1 - MVP Completion

### Vehicle Management

* [x] Add Vehicle
* [x] Edit Vehicle
* [x] Delete Vehicle
* [x] Vehicle validation
* [x] Vehicle name formatting

### Fuel Entries

* [x] Add Fuel Entry
* [x] Edit Fuel Entry
* [x] Delete Fuel Entry
* [x] Chronology validation
* [x] Soft warning for inserted entries
* [x] Previous-value defaults
* [x] Recalculate defaults when latest entry is deleted
* [x] Odometer value supports 0 km
* [x] Intelligent three-way fuel calculations
* [x] Editable fuel quantity
* [x] Numeric input validation
* [x] Crash prevention for invalid numeric input

### Dashboard

* [x] Basic Dashboard
* [x] Vehicle statistics
* [x] Current mileage
* [x] Average mileage
* [x] Cost per km
* [x] Recent fills
* [x] Empty recent-fills state

### History

* [x] Fuel History Screen
* [x] Dynamic entry numbers
* [x] Edit fuel entry
* [x] Delete fuel entry
* [x] Empty history state
* [x] Monthly grouping
* [ ] Filtering

### Settings

* [x] Theme setting
* [x] Distance unit setting
* [x] Fuel unit setting
* [x] Currency setting
* [x] Polished option picker dialogs

---

## Phase 2 - Data Safety

### Backup and Restore

* [x] Versioned backup model
* [x] JSON backup export
* [x] JSON backup import
* [x] Replace-style restore
* [x] Transaction-safe Room restore
* [x] Storage Access Framework integration
* [x] Export/import confirmation dialogs
* [x] JSON-only import guidance
* [x] Device-tested JSON export/import
* [x] Invalid import scenarios verified

### Export

* [x] CSV export
* [x] Post-export share option
* [x] Device-tested CSV export/share
* [ ] Excel export

### Future Cloud Options

* [ ] Optional cloud sync research
* [ ] Optional app-owned automatic local backups
* [ ] Google Drive backup research
* [ ] Cloud sync
* [ ] Multi-device support

**Note:** Google Drive SDK and sign-in remain intentionally excluded from the current backup strategy.

---

## Phase 3 - Usability Improvements

### Search and Filtering

* [ ] Search fuel history
* [ ] Filter by date
* [ ] Filter by mileage range
* [ ] Filter by cost range
* [ ] Sort history options

### Quality of Life

* [x] Reuse previous amount and fuel price
* [x] Recalculate defaults when latest entry is deleted
* [x] Empty states
* [ ] Quick add entry
* [ ] Recent fill details
* [ ] Better success/error feedback across all forms

### Visual Polish

* [ ] Final app icon
* [ ] Splash screen polish
* [ ] Final color and typography pass
* [ ] Small-screen layout QA
* [ ] Dark-theme QA
* [ ] About screen

---

## Phase 4 - Statistics and Insights

### Statistics Screen

* [ ] Statistics Screen
* [ ] Mileage charts
* [ ] Fuel cost charts
* [ ] Cost per km trend
* [ ] Fuel price trend
* [ ] Time filters
* [ ] Best mileage
* [ ] Worst mileage
* [ ] Total distance summary
* [ ] Total fuel summary
* [ ] Total cost summary

### Reports

* [ ] Monthly fuel summary
* [ ] Per-vehicle report
* [ ] Best/worst mileage insights
* [ ] Fuel consumption reports

---

## Phase 5 - Maintenance Tracker

### Service Records

* [ ] Service records
* [ ] Oil change tracking
* [ ] Insurance renewal tracking
* [ ] PUC reminders

### Notifications

* [ ] Service due reminders
* [ ] Insurance renewal alerts
* [ ] Maintenance notifications

---

## Phase 6 - Advanced and Fleet Features

### Fuel Insights

* [ ] Fuel price trends
* [ ] Cost per km trends
* [ ] Fuel consumption reports

### Synchronization

* [ ] Cloud sync
* [ ] Multi-device support

### Fleet Features

* [ ] Multiple drivers
* [ ] Fleet dashboard
* [ ] Shared vehicles

---

## Planned Screens

### Home Screen

* [x] Vehicle list
* [ ] Default vehicle
* [ ] Quick add vehicle

### Dashboard Screen

* [x] Current mileage
* [x] Vehicle statistics
* [x] Recent fuel fills
* [ ] Recent fill details

### Fuel Entry Screen

* [x] Add fuel entry
* [x] Edit fuel entry
* [x] Mileage calculation support
* [x] Intelligent three-way fuel calculator
* [x] Editable fuel quantity
* [x] Odometer supports 0 km
* [x] Numeric input validation

### Fuel History Screen

* [x] Fuel entries
* [x] Edit/delete
* [x] Monthly grouping
* [ ] Filtering

### Statistics Screen

* [ ] Trend charts
* [ ] Mileage analytics
* [ ] Fuel cost analysis

### Settings Screen

* [x] Backup and restore
* [x] Export data
* [x] Theme settings
* [ ] About

---

## Testing Checklist

### Functional Testing

* [x] Mileage calculation accuracy
* [x] Multi-vehicle switching
* [x] Fuel entry validation
* [x] Dashboard metrics
* [x] Backup export/import
* [x] CSV export
* [x] Restore rollback behavior

### UI Testing

* [ ] Small devices
* [ ] Tablets
* [ ] Landscape mode
* [ ] Dark theme
* [x] Empty states
* [x] Export/import flows

### Data Testing

* [ ] Migration testing
* [ ] Database integrity
* [x] Backup restoration
* [x] Restore with invalid JSON
* [x] Restore with unsupported backup version

---

## Play Store Release Checklist

### Core Functionality

* [x] Vehicle CRUD
* [x] Fuel Entry CRUD
* [x] Dashboard basics
* [x] History Screen
* [x] Settings Screen
* [x] Backup and Restore
* [ ] Statistics Screen

### Quality

* [x] Empty States
* [x] Basic error handling
* [x] Dark mode support
* [x] Backup/restore manual QA
* [ ] Final app icon
* [ ] Splash screen
* [ ] Privacy policy

### Store Assets

* [ ] Screenshots
* [ ] Feature graphic
* [ ] Store listing content
* [ ] Release notes

---

## Recommended Development Order

### Sprint 1 - App Identity and Polish

* Finalize launcher icon
* Finalize splash screen
* Add About screen
* Final pass on settings, empty states, and dialogs
* Small-device QA
* Dark-theme QA

### Sprint 2 - Statistics and Insights

* Build statistics screen
* Add mileage charts
* Add fuel-cost charts
* Add report summaries

### Sprint 3 - History Enhancements

* Add search
* Add filtering
* Add sorting

### Future Enhancements

* Optional tank capacity per vehicle
* Estimated fuel remaining
* Estimated driving range
* Overfill warnings

---

## Current Sprint

### Active Tasks

* Finalize app icon
* Finalize splash screen
* Add About screen
* Small-screen QA
* Dark-theme QA

### Next Milestone

Prepare Fuel Garage for a polished Play Store v1 release, then move into the Statistics screen and advanced insights.
