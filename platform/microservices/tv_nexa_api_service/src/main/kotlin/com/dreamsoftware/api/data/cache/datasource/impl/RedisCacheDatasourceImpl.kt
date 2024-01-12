package com.dreamsoftware.api.data.cache.datasource.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.cache.exception.CacheException
import com.dreamsoftware.model.RedisStorageConfig
import redis.clients.jedis.JedisCluster
import kotlin.jvm.Throws

/**
 * Implementation of the [ICacheDatasource] interface for storing and retrieving data from Redis.
 *
 * @property jedisCluster The JedisCluster instance used to interact with the Redis cluster.
 * @property redisStorageConfig The configuration for Redis storage, including cache TTL and prefix.
 */
internal class RedisCacheDatasourceImpl(
    private val jedisCluster: JedisCluster,
    private val redisStorageConfig: RedisStorageConfig
): ICacheDatasource<String> {

    /**
     * Saves data into the Redis cache with the specified [key] and [payload].
     *
     * @param key The key used to save the data.
     * @param payload The data to be saved in the cache.
     * @param ttlInSeconds Time-to-live (TTL) for the cache entry in seconds.
     * If not provided, the default TTL from [redisStorageConfig] is used.
     * @throws CacheException.InternalErrorException If an internal error occurs during the save operation.
     */
    override fun save(key: String, payload: String, ttlInSeconds: Long?) {
        runCatching {
            with(jedisCluster) {
                buildCacheKey(key).let {
                    set(it, payload)
                    expire(it, ttlInSeconds ?: redisStorageConfig.cacheTtlInSeconds)
                }
            }
        }.getOrElse {
            throw CacheException.InternalErrorException(
                message = "An error occurred when trying to save cache entry",
                cause = it
            )
        }
    }

    /**
     * Retrieves data from the Redis cache associated with the specified [key].
     *
     * @param key The key used to retrieve the data from the cache.
     * @return The retrieved data as a String.
     * @throws CacheException.ItemNotFoundException If the cache entry associated with the key is not found.
     * @throws CacheException.InternalErrorException If an internal error occurs during the retrieval.
     */
    @Throws(
        CacheException.ItemNotFoundException::class,
        CacheException.InternalErrorException::class
    )
    override fun find(key: String): String =
        jedisCluster.get(buildCacheKey(key)).also {
            if(it == null)
                throw CacheException.ItemNotFoundException("Cache entry not found")
        }

    /**
     * Deletes the cache entry associated with the specified [key].
     *
     * @param key The key used to delete the cache entry.
     * @throws CacheException.InternalErrorException If an error occurs during the deletion process.
     */
    @Throws(CacheException.InternalErrorException::class)
    override fun delete(key: String) {
        runCatching {
            jedisCluster.del(buildCacheKey(key))
        }.getOrElse {
            throw CacheException.InternalErrorException(
                message = "An error occurred when trying to remove cache entry",
                cause = it
            )
        }
    }

    /**
     * Checks whether a cache entry exists for the specified [key].
     *
     * @param key The key to check for the existence of the cache entry.
     * @return True if the cache entry exists, otherwise False.
     * @throws CacheException.InternalErrorException If an error occurs while checking the cache entry.
     */
    override fun exists(key: String): Boolean = runCatching {
        jedisCluster.exists(buildCacheKey(key))
    }.getOrElse {
        throw CacheException.InternalErrorException(
            message = "An error occurred when checking the cache entry",
            cause = it
        )
    }

    /**
     * Builds the cache key by appending the provided [key] with the configured prefix from [redisStorageConfig].
     *
     * @param key The base key used to build the cache key.
     * @return The constructed cache key.
     */
    private fun buildCacheKey(key: String): String =
        redisStorageConfig.cachePrefix + key
}