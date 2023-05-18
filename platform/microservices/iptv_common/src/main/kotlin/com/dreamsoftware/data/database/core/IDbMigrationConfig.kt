package com.dreamsoftware.data.database.core

interface IDbMigrationConfig {
    val schemaTableName: String?
    val schemaLocation: String?
}