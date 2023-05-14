package com.dreamsoftware.data.database.datasource.category.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.category.ICategoryDataSource
import com.dreamsoftware.data.database.entity.CategoryEntity
import com.dreamsoftware.data.database.entity.CategoryTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

internal class CategoryDataSourceImpl(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, CategoryEntity>
): ICategoryDataSource {

    override suspend fun getAll(): List<CategoryEntity> = withContext(Dispatchers.IO) {
        database.dbExec {
            CategoryTable.selectAll().map(mapper::map)
        }
    }
}