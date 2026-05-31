# Changelog

## Unreleased

### Added

- Backup and restore backend with versioned JSON format.
- Android Storage Access Framework export/import flow.
- Backup JSON export for full app restore.
- CSV export for spreadsheet-friendly vehicle and fuel-entry data.
- Post-export sharing through Android share sheet.
- Import guidance explaining that only backup JSON files can be restored.
- Settings backup/export screen with JSON and CSV choices.
- Fuel history empty state matching the vehicle empty state style.
- Dashboard recent-fills empty state.

### Changed

- Settings option dialogs now use clean rows with checkmarks instead of radio buttons.
- Settings option dialogs now include row separators for clearer selection.
- Export flow now uses confirmation dialogs before creating files.
- CSV export is clearly described as view/analyze only, not restore-capable.

### Verified

- `:app:compileDebugKotlin`
- `:app:testDebugUnitTest`

## v0.1.0

### Added

- Vehicle management.
- Fuel entry system.
- Room database.
- Mileage calculation.
- Dashboard metrics.
- Fuel history.
- Edit and delete support for vehicles and fuel entries.
- Vehicle and odometer chronology validation.
- Settings for theme, distance unit, fuel unit, and currency.
