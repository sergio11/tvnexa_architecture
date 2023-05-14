package com.dreamsoftware.data.database.datasource.subdivision.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDataSource
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

internal class SubdivisionDataSourceImpl(
    private val database: IDatabaseFactory,
    private val mapper: IOneSideMapper<ResultRow, SubdivisionEntity>
): ISubdivisionDataSource {
    override suspend fun getAll(): List<SubdivisionEntity> = withContext(Dispatchers.IO) {
        database.dbExec {
            SubdivisionTable.selectAll().map(mapper::map)
        }
    }
}