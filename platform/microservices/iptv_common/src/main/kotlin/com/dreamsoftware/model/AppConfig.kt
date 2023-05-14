package com.dreamsoftware.model

data class AppConfig(
    val dbConfig: DatabaseConfig,
    val iptvOrgConfig: IptvOrgConfig
)

data class DatabaseConfig(
    val driverClass: String,
    val databaseUrl: String,
    val connUser: String,
    val connPassword: String,
    val maxPoolSize: Int
)

data class IptvOrgConfig(
    val countriesEndpoint: String,
    val subdivisionsEndpoint: String,
    val languagesEndpoint: String,
    val categoriesEndpoint: String
)


