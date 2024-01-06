package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.api.domain.repository.ICategoryRepository
import com.dreamsoftware.data.database.entity.CategoryEntity

/**
 * Implementation of the [ICategoryRepository] interface that provides access to category data
 * from a database data source.
 *
 * @property categoriesDatabaseDataSource The data source responsible for retrieving category data.
 * @property cacheDatasource The cache data source used for caching channel data.
 */
internal class CategoryRepositoryImpl(
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), ICategoryRepository {

    private companion object {
        const val ALL_CATEGORIES_CACHE_KEY = "categories:all"
        const val CATEGORY_CACHE_KEY_PREFIX = "categories:"
    }

    /**
     * Retrieve all categories available in the repository.
     *
     * @return An iterable collection of all category entities.
     */
    override suspend fun findAll(): List<CategoryEntity> =
        retrieveFromCacheOrElse(cacheKey = ALL_CATEGORIES_CACHE_KEY) {
            categoriesDatabaseDataSource.findAll().toList()
        }

    /**
     * Retrieve a category by its unique identifier.
     *
     * @param id The unique identifier of the category to retrieve.
     * @return The category entity matching the specified ID.
     */
    override suspend fun findById(id: String): CategoryEntity? =
        retrieveFromCacheOrElse(cacheKey = CATEGORY_CACHE_KEY_PREFIX + id) {
            categoriesDatabaseDataSource.findByKey(id)
        }
}