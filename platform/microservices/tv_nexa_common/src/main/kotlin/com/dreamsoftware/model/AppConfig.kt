package com.dreamsoftware.model

/**
 * Data class representing the application configuration.
 *
 * @property dbConfig Configuration for the database.
 * @property iptvOrgConfig Configuration for IPTV organization.
 * @property epgGrabbingConfig Configuration for EPG grabbing.
 * @property redisClusterConfig Configuration for Redis cluster.
 */
data class AppConfig(
    val dbConfig: DatabaseConfig,
    val iptvOrgConfig: IptvOrgConfig,
    val epgGrabbingConfig: EpgGrabbingConfig,
    val redisClusterConfig: RedisClusterConfig
)

/**
 * Data class representing configuration for a database group.
 *
 * @property databaseUrl The URL for the database.
 * @property maxPoolSize The maximum pool size for the database connections.
 */
data class DatabaseGroupConfig(
    val databaseUrl: String,
    val maxPoolSize: Int
)

/**
 * Data class representing database configuration.
 *
 * @property driverClass The driver class for the database.
 * @property writeGroupConfig Configuration for the write database group.
 * @property readGroupConfig Configuration for the read database group.
 * @property connUser The username for database connection.
 * @property connPassword The password for database connection.
 */
data class DatabaseConfig(
    val driverClass: String,
    val writeGroupConfig: DatabaseGroupConfig,
    val readGroupConfig: DatabaseGroupConfig,
    val connUser: String,
    val connPassword: String,
)

/**
 * Data class representing configuration for IPTV organization endpoints.
 *
 * @property countriesEndpoint Endpoint for countries.
 * @property subdivisionsEndpoint Endpoint for subdivisions.
 * @property languagesEndpoint Endpoint for languages.
 * @property categoriesEndpoint Endpoint for categories.
 * @property regionsEndpoint Endpoint for regions.
 * @property channelsEndpoint Endpoint for channels.
 * @property channelStreamsEndpoint Endpoint for channel streams.
 * @property channelGuidesEndpoint Endpoint for channel guides.
 */
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

/**
 * Data class representing configuration for EPG grabbing.
 *
 * @property sitesBaseFolder Base folder for EPG sites.
 * @property jsConfigPath Path for JavaScript configuration.
 * @property channelsPath Path for channels.
 * @property outputGuidesPath Path for output guides.
 */
data class EpgGrabbingConfig(
    val sitesBaseFolder: String,
    val jsConfigPath: String,
    val channelsPath: String,
    val outputGuidesPath: String
)

/**
 * Data class representing configuration for a Redis cluster.
 *
 * @property storageConfig Configuration for Redis storage.
 * @property nodes List of Redis nodes' configurations.
 */
data class RedisClusterConfig(
    val storageConfig: RedisStorageConfig,
    val nodes: List<RedisNodeConfig>
)

/**
 * Data class representing configuration for Redis storage.
 *
 * @property cachePrefix The prefix used for caching in Redis.
 * @property cacheTtlInSeconds The default TTL in seconds
 */
data class RedisStorageConfig(val cachePrefix: String, val cacheTtlInSeconds: Long)

/**
 * Data class representing configuration for a Redis node.
 *
 * @property host The host of the Redis node.
 * @property port The port of the Redis node.
 */
data class RedisNodeConfig(val host: String, val port: Int)