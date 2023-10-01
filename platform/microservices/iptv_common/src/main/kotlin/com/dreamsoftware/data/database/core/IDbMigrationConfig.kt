package com.dreamsoftware.data.database.core

/**
 * The `IDbMigrationConfig` interface defines configuration options for database schema migrations.
 */
interface IDbMigrationConfig {
    /**
     * Gets the custom schema table name for tracking migration history, if provided.
     * If not provided, returns null.
     */
    val schemaTableName: String?

    /**
     * Gets the location of the database schema migrations. If not provided, returns null.
     * The location can be a file path or a classpath resource location.
     */
    val schemaLocation: String?

    /**
     * Indicates whether a generic schema (common schema for all environments) is used.
     * If true, `schemaLocation` is used directly. If false, environment-specific folders
     * are added to the `schemaLocation` based on the application's environment.
     */
    val genericSchema: Boolean
}