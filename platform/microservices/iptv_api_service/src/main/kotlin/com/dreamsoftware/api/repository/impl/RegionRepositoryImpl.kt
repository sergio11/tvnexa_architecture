package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IRegionRepository
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity

/**
 * Implementation of the [IRegionRepository] interface that provides access to region data
 * from a database data source.
 *
 * @property regionDatabaseDataSource The data source responsible for retrieving region data.
 */
class RegionRepositoryImpl(
    private val regionDatabaseDataSource: IRegionDatabaseDataSource
) : IRegionRepository {

    /**
     * Retrieve all regions available in the repository.
     *
     * @return An iterable collection of all region entities.
     */
    override suspend fun findAll(): Iterable<RegionEntity> =
        regionDatabaseDataSource.findAll()

    /**
     * Retrieve a region by its unique code.
     *
     * @param code The unique code of the region to retrieve.
     * @return The region entity matching the specified code.
     */
    override suspend fun findByCode(code: String): RegionEntity =
        regionDatabaseDataSource.findByKey(code)
}