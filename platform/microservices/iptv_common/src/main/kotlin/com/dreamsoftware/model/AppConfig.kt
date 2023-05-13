package com.dreamsoftware.model

data class AppConfig(
    val dbConfig: DatabaseConfig
)

data class DatabaseConfig(
    val driverClass: String,
    val url: String,
    val user: String,
    val password: String,
    val maxPoolSize: Int
)
