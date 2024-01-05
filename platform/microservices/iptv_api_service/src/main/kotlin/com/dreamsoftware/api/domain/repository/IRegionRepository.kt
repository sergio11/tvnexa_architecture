package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.RegionEntity

/**
 * Interface for accessing region data from a repository.
 */
interface IRegionRepository {

    /**
     * Retrieve all regions available in the repository.
     *
     * @return An iterable collection of all region entities.
     */
    suspend fun findAll(): List<RegionEntity>

    /**
     * Retrieve a region by its unique code.
     *
     * @param code The unique code of the region to retrieve.
     * @return The region entity matching the specified code.
     */
    suspend fun findByCode(code: String): RegionEntity?
}