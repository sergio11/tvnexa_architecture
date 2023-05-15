package com.dreamsoftware.data.database.datasource.category.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.category.ICategoryDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CategoryTable
import org.jetbrains.exposed.sql.ResultRow

internal class CategoryDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, CategoryEntity>
): SupportDatabaseDataSource<CategoryEntity, String>(database, mapper, CategoryTable), ICategoryDatabaseDataSource