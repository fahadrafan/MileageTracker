# 🛣 Fuel Garage Development Roadmap

------------------------------------------------------------------------

# Current Status

Fuel Garage now has a polished, feature-complete MVP with a modern
Material 3 interface.

Implemented:

-   Vehicle add, edit, delete
-   Fuel entry add, edit, delete
-   Intelligent three-way fuel calculator
-   Dynamic fuel history with entry numbers
-   Monthly grouped history
-   Dashboard statistics and recent fills
-   Vehicle and fuel-entry validation
-   Chronology validation with hard blocks
-   Soft warnings for inserted entries
-   Previous-value defaults
-   Live fuel calculations while typing
-   Settings for theme, units and currency
-   Local JSON backup and restore
-   CSV export
-   Android Storage Access Framework
-   Android share sheet integration
-   Dark theme support
-   Polished empty states
-   Animated onboarding FAB for first-time users
-   Complete UI refresh across Home, Dashboard, History and Fuel Entry
    screens

Fuel Garage remains:

-   Offline-first
-   Privacy-friendly
-   No accounts
-   No Firebase
-   No cloud dependency
-   No Google Drive SDK
-   No sign-in required

------------------------------------------------------------------------

# Phase 1 -- MVP Completion

## Vehicle Management

-   [x] Add Vehicle
-   [x] Edit Vehicle
-   [x] Delete Vehicle
-   [x] Vehicle validation
-   [x] Vehicle name formatting

## Fuel Entries

-   [x] Add Fuel Entry
-   [x] Edit Fuel Entry
-   [x] Delete Fuel Entry
-   [x] Chronology validation
-   [x] Soft warning for inserted entries
-   [x] Previous-value defaults
-   [x] Intelligent three-way fuel calculator
-   [x] Editable fuel quantity
-   [x] Numeric validation
-   [x] Odometer supports 0 km
-   [x] Crash prevention for invalid numeric input

## Dashboard

-   [x] Dashboard
-   [x] Vehicle statistics
-   [x] Hero mileage card
-   [x] Current mileage
-   [x] Average mileage
-   [x] Cost per km
-   [x] Recent fills
-   [x] Empty recent-fills state
-   [x] Dashboard UI refresh

## History

-   [x] Fuel History
-   [x] Monthly grouping
-   [x] Dynamic entry numbers
-   [x] Edit fuel entry
-   [x] Delete fuel entry
-   [x] Empty history state
-   [x] History UI refresh
-   [ ] Search
-   [ ] Filtering

## Settings

-   [x] Theme
-   [x] Distance Unit
-   [x] Fuel Unit
-   [x] Currency
-   [x] Polished option dialogs

------------------------------------------------------------------------

# Phase 2 -- Data Safety

## Backup & Restore

-   [x] Versioned backup model
-   [x] JSON export
-   [x] JSON restore
-   [x] Replace-style restore
-   [x] Transaction-safe Room restore
-   [x] SAF integration
-   [x] Confirmation dialogs
-   [x] JSON validation
-   [x] Device-tested export/import
-   [x] Invalid restore testing

## Export

-   [x] CSV export
-   [x] Share exported CSV
-   [ ] Excel export

Cloud functionality intentionally postponed.

------------------------------------------------------------------------

# Phase 3 -- UI Polish ✅

## Home Screen

-   [x] Vehicle card redesign
-   [x] Current mileage emphasis
-   [x] Improved empty mileage state
-   [x] Consistent FAB
-   [x] Better typography
-   [x] Material 3 polish

## Dashboard

-   [x] Hero card redesign
-   [x] Statistics polish
-   [x] Better visual hierarchy
-   [x] Recent fills polish
-   [x] Animated onboarding FAB

## History

-   [x] Redesigned fuel cards
-   [x] Odometer emphasis
-   [x] Compact fuel details
-   [x] Full Tank chip
-   [x] Better typography
-   [x] Animated onboarding FAB

## Fuel Entry

-   [x] Section headers
-   [x] Intelligent calculator helper text
-   [x] Full Tank switch
-   [x] Improved visual hierarchy

**Status:** UI Refresh Complete

------------------------------------------------------------------------

# Phase 4 -- Play Store Readiness

## About Screen

-   [ ] About screen
-   [ ] App version
-   [ ] Privacy-first message
-   [ ] Open source acknowledgements (optional)

## Branding

-   [ ] Final launcher icon
-   [ ] Splash screen polish
-   [ ] Feature graphic

## Legal

-   [ ] Privacy Policy
-   [ ] Play Store Data Safety form

## Store Assets

-   [ ] Screenshots
-   [ ] Store description
-   [ ] Release notes

------------------------------------------------------------------------

# Phase 5 -- Insights

> Dashboard already provides summary statistics. Instead of a
> traditional Statistics screen, Fuel Garage will evolve into an
> **Insights** screen.

-   [ ] Best mileage
-   [ ] Worst mileage
-   [ ] Average fuel price
-   [ ] Fuel price trends
-   [ ] Mileage trends
-   [ ] Cost trends
-   [ ] Monthly summaries
-   [ ] Charts
-   [ ] Time filters

------------------------------------------------------------------------

# Phase 6 -- Quality of Life

-   [ ] Search fuel history
-   [ ] Filter history
-   [ ] Sort history
-   [ ] Quick add entry
-   [ ] Recent fill details
-   [ ] Better success feedback

------------------------------------------------------------------------

# Phase 7 -- Maintenance Tracker

-   [ ] Service history
-   [ ] Oil changes
-   [ ] Insurance renewal
-   [ ] PUC tracking
-   [ ] Maintenance reminders

------------------------------------------------------------------------

# Phase 8 -- Future Enhancements

## Vehicle Enhancements

-   [ ] Tank capacity
-   [ ] Estimated fuel remaining
-   [ ] Estimated driving range
-   [ ] Overfill warnings

## Fleet & Sync

-   [ ] Automatic local backups
-   [ ] Optional cloud sync
-   [ ] Multi-device sync
-   [ ] Multiple drivers
-   [ ] Shared vehicles
-   [ ] Fleet dashboard

------------------------------------------------------------------------

# Testing Checklist

## Functional

-   [x] Mileage calculations
-   [x] Multi-vehicle switching
-   [x] Chronology validation
-   [x] Fuel calculator
-   [x] Dashboard metrics
-   [x] Backup & Restore
-   [x] CSV export

## UI

-   [x] Empty states
-   [x] Material 3 polish
-   [ ] Small devices
-   [ ] Tablets
-   [ ] Landscape
-   [ ] Dark-theme QA

## Data

-   [x] Restore rollback
-   [x] Invalid JSON
-   [x] Unsupported backup version
-   [ ] Migration testing

------------------------------------------------------------------------

# Play Store Release Checklist

## Core Features

-   [x] Vehicle CRUD
-   [x] Fuel Entry CRUD
-   [x] Dashboard
-   [x] History
-   [x] Settings
-   [x] Backup & Restore
-   [x] CSV Export

## Polish

-   [x] UI Refresh
-   [x] Empty States
-   [x] Animated onboarding FAB
-   [x] Dark Mode
-   [ ] About Screen
-   [ ] Final App Icon
-   [ ] Splash Screen
-   [ ] Privacy Policy

## Store Assets

-   [ ] Screenshots
-   [ ] Feature Graphic
-   [ ] Store Listing
-   [ ] Release Notes

------------------------------------------------------------------------

# Current Sprint

1.  About Screen
2.  Privacy Policy
3.  Final App Icon
4.  Splash Screen polish
5.  Small-screen QA
6.  Dark-theme QA
7.  Play Store assets

------------------------------------------------------------------------

# Next Milestone

🚀 **Fuel Garage v1.0 (Play Store Release)**

After the v1.0 release, development will focus on the new **Insights**
screen, history search/filtering, maintenance tracking, and additional
quality-of-life improvements.
