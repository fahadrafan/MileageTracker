# AI_HANDOFF.md

# Fuel Garage -- AI Development Handoff

> This document is the long-term memory of the Fuel Garage project. Read
> this document before making any architectural or UI changes.

------------------------------------------------------------------------

# Project Overview

**App Name:** Fuel Garage

**Package**

`com.example.mileagetracker`

**Technology**

-   Kotlin
-   Jetpack Compose
-   Material 3
-   MVVM
-   StateFlow
-   Room Database

## Philosophy

Fuel Garage is intentionally:

-   Offline-first
-   Privacy-friendly
-   No accounts
-   No Firebase
-   No Google Sign-In
-   No cloud dependency
-   Lightweight
-   Simple to maintain

The goal is to become the best offline fuel tracker rather than the app
with the most features.

------------------------------------------------------------------------

# Architecture

MVVM is used throughout.

UI → ViewModel → Repository → Room

Business logic lives in ViewModels and utility classes.

UI should remain presentation-only.

------------------------------------------------------------------------

# Core Design Principles

## Never Store Derived Data

Mileage is NEVER stored.

Statistics are NEVER stored.

Entry numbers are NEVER stored.

Everything is calculated on demand.

Reason:

Derived values easily become inconsistent after edits or restores.

------------------------------------------------------------------------

# Mileage Engine

The only place responsible for statistics is:

`MileageCalculator.kt`

It provides:

-   Estimated Mileage
-   Last Verified Mileage
-   Average Verified Mileage
-   Total Distance
-   Fuel Consumed
-   Total Spent
-   Cost/km
-   Last Refuel
-   Fuel Entry Count

Never duplicate these calculations elsewhere.

------------------------------------------------------------------------

# Chronology Rules

Chronology is based on **odometer**, not date.

History is ordered by highest odometer.

Entry numbers are dynamically generated.

Example

1000 km → Entry #1

1500 km → Entry #2

2000 km → Entry #3

Hard validation:

-   Duplicate odometer
-   Impossible chronology

Soft warning:

-   Entry inserted between existing readings

These rules are intentionally strict.

------------------------------------------------------------------------

# Fuel Calculator

The intelligent calculator supports:

-   Amount + Price → Quantity
-   Amount + Quantity → Price
-   Price + Quantity → Amount

Characteristics:

-   Live calculation
-   Uses last edited fields
-   Works while typing
-   No recursion
-   Works during edit mode

Do not redesign unless fixing bugs.

------------------------------------------------------------------------

# Backup Philosophy

JSON Version = 1

Restore sequence:

1.  Delete fuel entries
2.  Delete vehicles
3.  Restore vehicles
4.  Restore fuel entries
5.  Restore settings

Always transaction-safe.

------------------------------------------------------------------------

# UI Philosophy

Keep the UI:

-   Clean
-   Information dense
-   Material 3
-   Minimal animations
-   Consistent spacing
-   No gradients
-   No unnecessary decoration

Avoid feature creep.

------------------------------------------------------------------------

# Screen Status

## Home

Status: Frozen

Implemented:

-   Vehicle cards
-   Current mileage
-   Empty mileage state
-   Improved typography
-   Consistent FAB

## Dashboard

Status: Frozen

Implemented:

-   Hero card
-   Statistics
-   Recent fills
-   Animated onboarding FAB
-   Edit/Delete actions

## History

Status: Frozen

Implemented:

-   Monthly grouping
-   Dynamic entry numbers
-   Redesigned cards
-   Full Tank chip
-   Animated onboarding FAB

## Fuel Entry

Status: Frozen

Implemented:

-   Refuel Details section
-   Fuel Details section
-   Intelligent calculator helper text
-   Full Tank switch
-   Improved hierarchy

## Settings

Implemented:

-   Theme
-   Units
-   Currency
-   Backup & Restore
-   CSV Export

About screen still pending.

------------------------------------------------------------------------

# Animated FAB

History and Dashboard use the same onboarding behaviour.

Timeline:

-   Start collapsed (+)
-   Wait \~1 second
-   Expand with animation
-   Stay expanded \~5 seconds
-   Collapse automatically

Only shown when there are no fuel entries.

------------------------------------------------------------------------

# Completed Features

-   Vehicle CRUD
-   Fuel Entry CRUD
-   Intelligent fuel calculator
-   Chronology validation
-   Dashboard
-   History
-   Backup & Restore
-   CSV Export
-   Settings
-   UI Refresh
-   Empty states
-   Animated onboarding FAB

------------------------------------------------------------------------

# Intentionally Deferred

Statistics screen.

Reason:

Dashboard already exposes summary statistics.

Future work should build an **Insights** screen instead.

Ideas:

-   Best/Worst mileage
-   Fuel trends
-   Cost trends
-   Charts
-   Time filters

------------------------------------------------------------------------

# Development Workflow

Before major changes:

1.  Inspect existing architecture.
2.  Keep implementation simple.
3.  Reuse existing logic.
4.  List affected files.
5.  Propose a plan.
6.  Wait for approval.
7.  Prefer small commits.

------------------------------------------------------------------------

# Things Future AI Must NOT Do

❌ Store mileage.

❌ Store statistics.

❌ Store entry numbers.

❌ Duplicate MileageCalculator logic.

❌ Break chronology validation.

❌ Replace Material 3 with custom design language.

❌ Introduce unnecessary complexity.

------------------------------------------------------------------------

# Current Priorities

1.  About Screen
2.  Privacy Policy
3.  Final launcher icon
4.  Splash screen polish
5.  Small-screen QA
6.  Dark-theme QA
7.  Play Store assets

After v1.0:

-   Insights
-   Search
-   Filtering
-   Maintenance tracker

------------------------------------------------------------------------

# Vision

Fuel Garage should remain a focused utility.

Every feature should answer one question:

**Does this make tracking fuel simpler without compromising privacy or
maintainability?**

If the answer is no, do not add it.

The long-term goal is quality, reliability and simplicity---not feature
count.
