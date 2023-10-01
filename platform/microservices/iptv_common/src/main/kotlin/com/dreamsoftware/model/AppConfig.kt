package com.dreamsoftware.model

data class AppConfig(
    val dbConfig: DatabaseConfig,
    val iptvOrgConfig: IptvOrgConfig,
    val epgGrabbingConfig: EpgGrabbingConfig
)

data class DatabaseGroupConfig(
    val databaseUrl: String,
    val maxPoolSize: Int
)

data class DatabaseConfig(
    val driverClass: String,
    val writeGroupConfig: DatabaseGroupConfig,
    val readGroupConfig: DatabaseGroupConfig,
    val connUser: String,
    val connPassword: String,

)

data class IptvOrgConfig(
    val countriesEndpoint: String,
    val subdivisionsEndpoint: String,
    val languagesEndpoint: String,
    val categoriesEndpoint: String,
    val regionsEndpoint: String,
    val channelsEndpoint: String,
    val channelStreamsEndpoint: String,
    val channelGuidesEndpoint: String
)

data class EpgGrabbingConfig(
    val sitesBaseFolder: String,
    val jsConfigPath: String,
    val channelsPath: String,
    val outputGuidesPath: String
)
