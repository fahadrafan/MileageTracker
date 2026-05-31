# 🛣 Development Roadmap

---

## Current Status

Fuel Garage now has a strong MVP foundation:

- Vehicle add, edit, delete
- Fuel entry add, edit, delete
- Dynamic fuel history with entry numbers
- Dashboard statistics and recent fills
- Vehicle and fuel-entry validation
- Settings for theme, units, and currency
- Local backup and restore using JSON
- CSV export for spreadsheet-friendly data
- Android Storage Access Framework export/import flow
- Post-export sharing through Android share sheet
- Polished empty states for vehicles and fuel entries

The app is still offline-first and does not require accounts, Firebase, Google
Drive SDK, or cloud sign-in.

---

## Phase 1 - MVP Completion

### Vehicle Management

- [x] Add Vehicle
- [x] Edit Vehicle
- [x] Delete Vehicle
- [x] Vehicle validation
- [x] Vehicle name formatting

### Fuel Entries

- [x] Add Fuel Entry
- [x] Edit Fuel Entry
- [x] Delete Fuel Entry
- [x] Fuel quantity auto-calculation
- [x] Chronology validation
- [x] Soft warning for inserted entries

### Dashboard

- [x] Basic Dashboard
- [x] Vehicle statistics
- [x] Current mileage
- [x] Average mileage
- [x] Cost per km
- [x] Recent fills
- [x] Empty recent-fills state

### History

- [x] Fuel History Screen
- [x] Dynamic entry numbers
- [x] Edit fuel entry
- [x] Delete fuel entry
- [x] Empty history state
- [ ] Monthly grouping
- [ ] Filtering

### Settings

- [x] Theme setting
- [x] Distance unit setting
- [x] Fuel unit setting
- [x] Currency setting
- [x] Polished option picker dialogs

---

## Phase 2 - Data Safety

### Backup and Restore

- [x] Versioned backup model
- [x] JSON backup export
- [x] JSON backup import
- [x] Replace-style restore
- [x] Transaction-safe Room restore
- [x] Storage Access Framework integration
- [x] Export/import confirmation dialogs
- [x] JSON-only import guidance

### Export

- [x] CSV export
- [x] Post-export share option
- [ ] Excel export

### Future Cloud Options

- [ ] Optional cloud sync research
- [ ] Optional app-owned automatic local backups
- [ ] Google Drive backup research
- [ ] Cloud sync
- [ ] Multi-device support

Note: Google Drive SDK and sign-in are intentionally not part of the current
backup strategy.

---

## Phase 3 - Usability Improvements

### Search and Filtering

- [ ] Search fuel history
- [ ] Filter by date
- [ ] Filter by mileage range
- [ ] Filter by cost range
- [ ] Sort history options

### Quality of Life

- [x] Reuse previous amount and fuel price
- [x] Recalculate defaults when latest entry is deleted
- [x] Empty states
- [ ] Quick add entry
- [ ] Recent fill details
- [ ] Better success/error feedback across all forms
- [ ] Confirmation before risky restore/import actions

### Visual Polish

- [ ] App icon
- [ ] Splash screen
- [ ] Final color and typography pass
- [ ] Small-screen layout QA
- [ ] Dark-theme QA

---

## Phase 4 - Statistics and Insights

### Statistics Screen

- [ ] Statistics Screen
- [ ] Mileage charts
- [ ] Fuel cost charts
- [ ] Cost per km trend
- [ ] Fuel price trend
- [ ] Time filters
- [ ] Best mileage
- [ ] Worst mileage
- [ ] Total distance summary
- [ ] Total fuel summary
- [ ] Total cost summary

### Reports

- [ ] Monthly fuel summary
- [ ] Per-vehicle report
- [ ] Best/worst mileage insights
- [ ] Fuel consumption reports

---

## Phase 5 - Maintenance Tracker

### Service Records

- [ ] Service records
- [ ] Oil change tracking
- [ ] Insurance renewal tracking
- [ ] PUC reminders

### Notifications

- [ ] Service due reminders
- [ ] Insurance renewal alerts
- [ ] Maintenance notifications

---

## Phase 6 - Advanced and Fleet Features

### Fuel Insights

- [ ] Fuel price trends
- [ ] Cost per km trends
- [ ] Fuel consumption reports

### Synchronization

- [ ] Cloud sync
- [ ] Multi-device support

### Fleet Features

- [ ] Multiple drivers
- [ ] Fleet dashboard
- [ ] Shared vehicles

---

## Planned Screens

### Home Screen

- [x] Vehicle list
- [ ] Default vehicle
- [ ] Quick add vehicle

### Dashboard Screen

- [x] Current mileage
- [x] Vehicle statistics
- [x] Recent fuel fills
- [ ] Recent fill details

### Fuel Entry Screen

- [x] Add fuel entry
- [x] Edit fuel entry
- [x] Mileage calculation support

### Fuel History Screen

- [x] Fuel entries
- [x] Edit/delete
- [ ] Grouped entries
- [ ] Filtering

### Statistics Screen

- [ ] Trend charts
- [ ] Mileage analytics
- [ ] Fuel cost analysis

### Settings Screen

- [x] Backup and restore
- [x] Export data
- [x] Theme settings
- [ ] About

---

## Testing Checklist

### Functional Testing

- [ ] Mileage calculation accuracy
- [ ] Multi-vehicle switching
- [ ] Fuel entry validation
- [ ] Dashboard metrics
- [ ] Backup export/import
- [ ] CSV export
- [ ] Restore rollback behavior

### UI Testing

- [ ] Small devices
- [ ] Tablets
- [ ] Landscape mode
- [ ] Dark theme
- [ ] Empty states
- [ ] Export/import flows

### Data Testing

- [ ] Migration testing
- [ ] Database integrity
- [ ] Backup restoration
- [ ] Restore with invalid JSON
- [ ] Restore with unsupported backup version

---

## Play Store Release Checklist

### Core Functionality

- [x] Vehicle CRUD
- [x] Fuel Entry CRUD
- [x] Dashboard basics
- [x] History Screen
- [x] Settings Screen
- [x] Backup and Restore
- [ ] Statistics Screen

### Quality

- [x] Empty States
- [x] Basic error handling
- [x] Dark mode support
- [ ] App icon
- [ ] Splash screen
- [ ] Privacy policy
- [ ] Backup/restore manual QA

### Store Assets

- [ ] Screenshots
- [ ] Feature graphic
- [ ] Store listing content
- [ ] Release notes

---

## Recommended Development Order

### Sprint 1 - Stabilize Backup and Export

- Test JSON export/import on device
- Test CSV export and share flow
- Add backup-specific tests
- Improve restore error messages

### Sprint 2 - App Identity and Polish

- Add app icon
- Add splash screen
- Final pass on settings, empty states, and dialogs
- Small-device and dark-theme QA

### Sprint 3 - History Improvements

- Add history search
- Add date filtering
- Add monthly grouping

### Sprint 4 - Statistics

- Build statistics screen
- Add mileage and fuel-cost charts
- Add report summaries

---

## Current Sprint

### Active Tasks

- Device-test backup JSON export/import
- Device-test CSV export and share
- Polish Settings and empty states

### Next Milestone

Stabilize the backup/export release candidate, then move to app icon and launch
polish before starting the Statistics screen.
