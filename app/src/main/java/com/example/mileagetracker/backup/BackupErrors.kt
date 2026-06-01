package com.example.mileagetracker.backup

open class BackupError(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

class EmptyBackupDataError : BackupError(
    "There is no vehicle data to export. Please add at least one vehicle first."
)

class InvalidBackupFileError(
    cause: Throwable? = null
) : BackupError(
    "This file is not a valid Fuel Garage backup file, please choose a valid JSON file to import.",
    cause
)

class UnsupportedBackupVersionError(
    version: Int
) : BackupError(
    "This backup version ($version) is not supported by this app."
)

class EmptyRestoreDataError : BackupError(
    "This backup does not contain any vehicles. Please choose a backup with at least one vehicle."
)

class RestoreFailedError(
    cause: Throwable? = null
) : BackupError(
    "Restore failed. Your existing data was not changed.",
    cause
)
