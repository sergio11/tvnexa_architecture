package com.dreamsoftware.api.data.respository.impl.core

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.utils.fromJson
import com.dreamsoftware.api.utils.toJSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class SupportRepository(
    val cacheDatasource: ICacheDatasource<String>
) {

    /**
     * Retrieves a value from the cache based on the specified [cacheKey]. If the cache entry
     * is not found, performs the [onCacheEntryNotFound] operation and saves the result to the cache.
     *
     * @param cacheKey The key used to retrieve the value from the cache.
     * @param onCacheEntryNotFound The suspendable operation to execute when the cache entry is not found.
     * @return The retrieved value from the cache or the result of [onCacheEntryNotFound].
     */
    suspend inline fun <reified T> retrieveFromCacheOrElse(
        cacheKey: String,
        crossinline onCacheEntryNotFound: suspend () -> T
    ): T = withContext(Dispatchers.IO) {
        with(cacheDatasource) {
            runCatching {
                find(cacheKey).fromJson<T>()
            }.getOrElse {
                onCacheEntryNotFound().also {
                    runCatching {
                        save(cacheKey, it.toJSON())
                    }
                }
            }
        }
    }
}