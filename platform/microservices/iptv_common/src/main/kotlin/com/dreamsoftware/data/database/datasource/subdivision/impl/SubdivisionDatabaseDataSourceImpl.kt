package com.dreamsoftware.data.database.datasource.subdivision.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.subdivision.ISubdivisionDatabaseDataSource
import com.dreamsoftware.data.database.entity.SubdivisionEntity
import com.dreamsoftware.data.database.entity.SubdivisionTable
import org.jetbrains.exposed.sql.ResultRow

internal class SubdivisionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<ResultRow, SubdivisionEntity>
): SupportDatabaseDataSource<SubdivisionEntity, String>(database, mapper, SubdivisionTable), ISubdivisionDatabaseDataSource