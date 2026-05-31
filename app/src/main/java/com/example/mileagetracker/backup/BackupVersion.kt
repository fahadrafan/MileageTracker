package com.example.mileagetracker.backup

object BackupVersion {
    const val CURRENT = 1

    fun isSupported(version: Int): Boolean {
        return version == CURRENT
    }
}
