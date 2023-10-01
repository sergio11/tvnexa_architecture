package com.dreamsoftware.data.database.datasource.category.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.CategoryTable
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * The `CategoryDatabaseDataSourceImpl` class implements the `ICategoryDatabaseDataSource` interface
 * and provides methods for interacting with the database related to category entities.
 *
 * @param database The database factory used to manage database connections.
 * @param mapper The mapper used to convert between database entities and domain entities.
 */
internal class CategoryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<CategoryEntityDAO, CategoryEntity>
) : SupportDatabaseDataSource<String, CategoryEntityDAO, SaveCategoryEntity, CategoryEntity>(
    database,
    mapper,
    CategoryEntityDAO
), ICategoryDatabaseDataSource {

    /**
     * Maps a category entity to its corresponding database representation for saving or updating.
     *
     * @param entityToSave The category entity to save or update.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveCategoryEntity) = with(entityToSave) {
        this@onMapEntityToSave[CategoryTable.name] = name
        this@onMapEntityToSave[CategoryTable.categoryId] = id
    }
}