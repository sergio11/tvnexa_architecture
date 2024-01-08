package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.ISubdivisionRepository
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SubdivisionEntity

/**
 * Implementation of [ISubdivisionRepository] that handles Subdivision entities.
 *
 * @property subdivisionDatabaseDataSource The data source for Subdivision entities.
 * @property cacheDatasource The cache data source used for caching.
 */
internal class SubdivisionRepositoryImpl(
    private val subdivisionDatabaseDataSource: ISubdivisionDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), ISubdivisionRepository {

    private companion object {
        const val ALL_SUBDIVISIONS_CACHE_KEY = "subdivisions:all"
        const val SUBDIVISION_CACHE_KEY_PREFIX = "subdivisions:"
    }

    /**
     * Retrieves all Subdivision entities.
     *
     * @return A list of all SubdivisionEntity objects.
     */
    override suspend fun findAll(): List<SubdivisionEntity> =
        retrieveFromCacheOrElse(cacheKey = ALL_SUBDIVISIONS_CACHE_KEY) {
            subdivisionDatabaseDataSource.findAll().toList()
        }

    /**
     * Retrieves a Subdivision entity by its code.
     *
     * @param code The unique code identifying the Subdivision.
     * @return The SubdivisionEntity object if found, otherwise null.
     */
    override suspend fun findByCode(code: String): SubdivisionEntity? =
        retrieveFromCacheOrElse(cacheKey = "$SUBDIVISION_CACHE_KEY_PREFIX$code") {
            subdivisionDatabaseDataSource.findByKey(code)
        }
}
