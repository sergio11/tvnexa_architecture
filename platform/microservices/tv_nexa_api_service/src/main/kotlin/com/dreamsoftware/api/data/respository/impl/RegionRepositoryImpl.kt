package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IRegionRepository
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity

/**
 * Implementation of the [IRegionRepository] interface that provides access to region data
 * from a database data source.
 *
 * @property regionDatabaseDataSource The data source responsible for retrieving region data.
 * @property cacheDatasource The cache data source used for caching channel data.
 */
internal class RegionRepositoryImpl(
    private val regionDatabaseDataSource: IRegionDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), IRegionRepository {

    private companion object {
        const val ALL_REGIONS_CACHE_KEY = "regions:all"
        const val REGION_CACHE_KEY_PREFIX = "regions:"
    }

    /**
     * Retrieve all regions available in the repository.
     *
     * @return An iterable collection of all region entities.
     */
    override suspend fun findAll(): List<RegionEntity> =
        retrieveFromCacheOrElse(cacheKey = ALL_REGIONS_CACHE_KEY) {
            regionDatabaseDataSource.findAll().toList()
        }

    /**
     * Retrieve a region by its unique code.
     *
     * @param code The unique code of the region to retrieve.
     * @return The region entity matching the specified code.
     */
    override suspend fun findByCode(code: String): RegionEntity? =
        retrieveFromCacheOrElse(cacheKey = REGION_CACHE_KEY_PREFIX) {
            regionDatabaseDataSource.findByKey(code)
        }
}