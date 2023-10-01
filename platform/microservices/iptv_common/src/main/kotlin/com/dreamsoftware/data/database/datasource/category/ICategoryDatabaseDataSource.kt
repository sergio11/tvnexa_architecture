package com.dreamsoftware.data.database.datasource.category

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.SaveCategoryEntity

/**
 * The `ICategoryDatabaseDataSource` interface defines methods for interacting with the database
 * related to category entities. It extends the `ISupportDatabaseDataSource` interface with specific
 * types for category-related operations.
 *
 * @param K The type of the category entity's identifier (e.g., String).
 * @param S The type of the entity used for saving or updating a category (e.g., SaveCategoryEntity).
 * @param T The type of the category entity (e.g., CategoryEntity).
 */
interface ICategoryDatabaseDataSource : ISupportDatabaseDataSource<String, SaveCategoryEntity, CategoryEntity>