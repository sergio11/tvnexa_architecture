package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.SubdivisionEntity

/**
 * Repository interface for handling Subdivision entities.
 */
interface ISubdivisionRepository {

    /**
     * Retrieves all Subdivision entities.
     *
     * @return A list of all SubdivisionEntity objects.
     */
    suspend fun findAll(): List<SubdivisionEntity>

    /**
     * Retrieves a Subdivision entity by its code.
     *
     * @param code The unique code identifying the Subdivision.
     * @return The SubdivisionEntity object if found, otherwise null.
     */
    suspend fun findByCode(code: String): SubdivisionEntity?
}