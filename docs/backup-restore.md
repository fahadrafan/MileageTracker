# Backup and Restore

Phase 1 is a storage-agnostic engine. It does not use Google Drive SDK,
sign-in, Firebase, or Android file pickers.

`BackupManager.exportJson()` returns a versioned JSON string containing:

- vehicles
- fuel entries
- user settings

`RestoreManager.restore(jsonText)` parses the JSON, checks the backup version,
then replaces local Room data inside a database transaction:

1. delete fuel entries
2. delete vehicles
3. insert vehicles
4. insert fuel entries

Settings are restored through DataStore after the database transaction. A later
UI layer can pass this JSON to Android Storage Access Framework for import and
export without changing the backup format.
