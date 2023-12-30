package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Interface for accessing category data from a repository.
 */
interface ICategoryRepository {

    /**
     * Retrieve all categories available in the repository.
     *
     * @return An iterable collection of all category entities.
     */
    suspend fun findAll(): List<CategoryEntity>

    /**
     * Retrieve a category by its unique identifier.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return The category entity matching the specified ID.
     */
    suspend fun findById(id: String): CategoryEntity?
}
