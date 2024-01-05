package com.dreamsoftware.api.data.cache.datasource.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.cache.exception.CacheException
import com.dreamsoftware.model.RedisStorageConfig
import com.google.gson.Gson
import redis.clients.jedis.JedisCluster
import kotlin.jvm.Throws

class RedisCacheDatasourceImpl(
    private val jedisCluster: JedisCluster,
    private val redisStorageConfig: RedisStorageConfig
): ICacheDatasource<String> {

    override fun <E> save(key: String, payload: E) {
        runCatching {
            with(jedisCluster) {
                set(buildCacheKey(key), Gson().toJson(payload))
            }
        }.getOrElse {
            throw CacheException.InternalErrorException(
                message = "An error occurred when trying to save cache entry",
                cause = it
            )
        }
    }

    @Throws(
        CacheException.ItemNotFoundException::class,
        CacheException.InternalErrorException::class
    )
    override fun <E> find(key: String, payloadClazz: Class<E>): E =
        jedisCluster.get(buildCacheKey(key)).let {
            if(it == null)
                throw CacheException.ItemNotFoundException("Cache entry not found")
            Gson().fromJson(it, payloadClazz)
        }

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

    override fun exists(key: String): Boolean = runCatching {
        jedisCluster.exists(buildCacheKey(key))
    }.getOrElse {
        throw CacheException.InternalErrorException(
            message = "An error occurred when checking the cache entry",
            cause = it
        )
    }

    private fun buildCacheKey(key: String): String =
        redisStorageConfig.cachePrefix + key
}