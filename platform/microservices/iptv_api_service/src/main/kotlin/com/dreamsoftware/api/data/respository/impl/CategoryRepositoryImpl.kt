package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.api.domain.repository.ICategoryRepository
import com.dreamsoftware.data.database.entity.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the [ICategoryRepository] interface that provides access to category data
 * from a database data source.
 *
 * @property categoriesDatabaseDataSource The data source responsible for retrieving category data.
 */
internal class CategoryRepositoryImpl(
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
) : ICategoryRepository {

    /**
     * Retrieve all categories available in the repository.
     *
     * @return An iterable collection of all category entities.
     */
    override suspend fun findAll(): List<CategoryEntity> = withContext(Dispatchers.IO) {
        categoriesDatabaseDataSource.findAll().toList()
    }

    /**
     * Retrieve a category by its unique identifier.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return The category entity matching the specified ID.
     */
    override suspend fun findById(id: String): CategoryEntity? = withContext(Dispatchers.IO) {
        categoriesDatabaseDataSource.findByKey(id)
    }
}