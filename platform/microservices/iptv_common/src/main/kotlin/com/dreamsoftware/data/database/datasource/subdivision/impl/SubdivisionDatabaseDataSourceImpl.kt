package com.dreamsoftware.data.database.datasource.subdivision.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.SubdivisionEntityDAO
import com.dreamsoftware.data.database.dao.SubdivisionTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveSubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class SubdivisionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity>
): SupportDatabaseDataSource<String, SubdivisionEntityDAO, SaveSubdivisionEntity , SubdivisionEntity>(database, mapper, SubdivisionEntityDAO), ISubdivisionDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveSubdivisionEntity) = with(entityToSave) {
        this@onMapEntityToSave[SubdivisionTable.code] = code
        this@onMapEntityToSave[SubdivisionTable.name] = name
        this@onMapEntityToSave[SubdivisionTable.country] = country
    }
}