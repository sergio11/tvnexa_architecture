package com.dreamsoftware.api.data.cache.datasource

import com.dreamsoftware.api.data.cache.exception.CacheException
import kotlin.jvm.Throws

/**
 * Interface defining operations for a cache datasource.
 *
 * @param K The type of keys used in the cache.
 */
interface ICacheDatasource<K> {

    /**
     * Saves data into the cache with a specified key.
     *
     * @param key The key used to save the data.
     * @param payload The data to be saved.
     * @return Boolean indicating whether the save operation was successful.
     * @throws CacheException.InternalErrorException If an internal error occurs during the save operation.
     */
    @Throws(CacheException.InternalErrorException::class)
    fun <E> save(key: K, payload: E)

    /**
     * Retrieves data from the cache using the specified key.
     *
     * @param key The key used to retrieve the data.
     * @return The retrieved data.
     * @throws CacheException.ItemNotFoundException If the item associated with the key is not found in the cache.
     * @throws CacheException.InternalErrorException If an internal error occurs during the retrieval operation.
     */
    @Throws(
        CacheException.ItemNotFoundException::class,
        CacheException.InternalErrorException::class
    )
    fun <E> find(key: K, payloadClazz: Class<E>): E

    /**
     * Deletes data from the cache using the specified key.
     *
     * @param key The key used to delete the data.
     * @throws CacheException.InternalErrorException If an internal error occurs during the delete operation.
     */
    @Throws(CacheException.InternalErrorException::class)
    fun delete(key: K)

    /**
     * Checks if data exists in the cache using the specified key.
     *
     * @param key The key to check for existence in the cache.
     * @return Boolean indicating whether data exists for the provided key.
     */
    fun exists(key: K): Boolean
}
