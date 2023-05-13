package com.dreamsoftware.model

data class AppConfig(
    val dbConfig: DatabaseConfig
)

data class DatabaseConfig(
    val driverClass: String,
    val databaseUrl: String,
    val connUser: String,
    val connPassword: String,
    val maxPoolSize: Int
)
