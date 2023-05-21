package com.dreamsoftware.data.database.datasource.category.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.CategoryEntityDAO
import com.dreamsoftware.data.database.dao.CategoryTable
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.SaveCategoryEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class CategoryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<CategoryEntityDAO, CategoryEntity>
): SupportDatabaseDataSource<String, CategoryEntityDAO, SaveCategoryEntity, CategoryEntity>(database, mapper, CategoryEntityDAO), ICategoryDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveCategoryEntity) = with(entityToSave) {
        this@onMapEntityToSave[CategoryTable.name] = name
        this@onMapEntityToSave[CategoryTable.categoryId] = id
    }
}