package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.api.repository.ICategoryRepository
import com.dreamsoftware.data.database.entity.CategoryEntity

class CategoryRepositoryImpl(
    private val categoriesDatabaseDataSource: ICategoryDatabaseDataSource
): ICategoryRepository {
    override suspend fun findAll(): Iterable<CategoryEntity> =
        categoriesDatabaseDataSource.findAll()

    override suspend fun findById(id: String): CategoryEntity =
        categoriesDatabaseDataSource.findByKey(id)
}