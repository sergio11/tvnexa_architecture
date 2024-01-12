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

/**
 * Implementation of the [ISubdivisionDatabaseDataSource] interface for managing subdivision data in a database.
 *
 * @param database The [IDatabaseFactory] instance for accessing the database.
 * @param mapper The mapper to convert between the database entity ([SubdivisionEntityDAO]) and domain entity ([SubdivisionEntity]).
 */
internal class SubdivisionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<SubdivisionEntityDAO, SubdivisionEntity>
) : SupportDatabaseDataSource<String, SubdivisionEntityDAO, SaveSubdivisionEntity, SubdivisionEntity>(
    database,
    mapper,
    SubdivisionEntityDAO
), ISubdivisionDatabaseDataSource {

    /**
     * Maps a [SaveSubdivisionEntity] to a database update builder for saving to the database.
     *
     * @param entityToSave The [SaveSubdivisionEntity] to be mapped.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveSubdivisionEntity) = with(entityToSave) {
        this@onMapEntityToSave[SubdivisionTable.code] = code
        this@onMapEntityToSave[SubdivisionTable.name] = name
        this@onMapEntityToSave[SubdivisionTable.country] = country
    }
}