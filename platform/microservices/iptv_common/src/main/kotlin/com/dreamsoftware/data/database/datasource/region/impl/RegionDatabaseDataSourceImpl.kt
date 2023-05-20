package com.dreamsoftware.data.database.datasource.region.impl

import com.dreamsoftware.core.IOneSideMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.RegionEntityDAO
import com.dreamsoftware.data.database.dao.RegionTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class RegionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IOneSideMapper<RegionEntityDAO, RegionEntity>
): SupportDatabaseDataSource<RegionEntityDAO, String, RegionEntity>(database, mapper, RegionEntityDAO), IRegionDatabaseDataSource {
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: RegionEntity) = with(entityToSave) {
        this@onMapEntityToSave[RegionTable.code] = code
        this@onMapEntityToSave[RegionTable.name] = name
    }
}