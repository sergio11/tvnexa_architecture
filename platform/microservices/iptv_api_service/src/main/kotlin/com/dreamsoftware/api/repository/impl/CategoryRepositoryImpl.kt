package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.api.repository.ICategoryRepository
import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Implementation of the [ICategoryRepository] interface that provides access to category data
 * from a database data source.
 *
 * @property categoriesDatabaseDataSource The data source responsible for retrieving category data.
 */
class CategoryRepositoryImpl(
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
) : ICategoryRepository {

    /**
     * Retrieve all categories available in the repository.
     *
     * @return An iterable collection of all category entities.
     */
    override suspend fun findAll(): Iterable<CategoryEntity> =
        categoriesDatabaseDataSource.findAll()

    /**
     * Retrieve a category by its unique identifier.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return The category entity matching the specified ID.
     */
    override suspend fun findById(id: String): CategoryEntity =
        categoriesDatabaseDataSource.findByKey(id)
}