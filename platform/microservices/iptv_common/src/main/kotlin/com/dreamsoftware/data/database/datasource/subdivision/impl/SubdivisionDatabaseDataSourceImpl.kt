package com.dreamsoftware.data.database.datasource.subdivision.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class SubdivisionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, SubdivisionEntity>
): SupportDatabaseDataSource<SubdivisionEntity, String>(database, mapper, SubdivisionTable), ISubdivisionDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entity: SubdivisionEntity) = with(entity) {
        this@onMapEntityToSave[SubdivisionTable.code] = code
        this@onMapEntityToSave[SubdivisionTable.name] = name
        this@onMapEntityToSave[SubdivisionTable.country] = country
    }

}