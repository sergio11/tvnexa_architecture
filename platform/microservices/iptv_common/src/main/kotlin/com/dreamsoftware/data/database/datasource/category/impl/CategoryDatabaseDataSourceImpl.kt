package com.dreamsoftware.data.database.datasource.category.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CategoryTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class CategoryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, CategoryEntity>
): SupportDatabaseDataSource<CategoryEntity, String>(database, mapper, CategoryTable), ICategoryDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entity: CategoryEntity) = with(entity) {
        this@onMapEntityToSave[CategoryTable.name] = name
        this@onMapEntityToSave[CategoryTable.categoryId] = id
    }

}