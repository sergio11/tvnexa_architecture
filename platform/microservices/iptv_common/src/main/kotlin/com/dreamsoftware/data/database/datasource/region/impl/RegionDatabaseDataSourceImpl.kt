package com.dreamsoftware.data.database.datasource.region.impl

import com.dreamsoftware.core.IMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.RegionCountryTable
import com.dreamsoftware.data.database.dao.RegionEntityDAO
import com.dreamsoftware.data.database.dao.RegionTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.region.IRegionDatabaseDataSource
import com.dreamsoftware.data.database.entity.RegionEntity
import com.dreamsoftware.data.database.entity.SaveRegionEntity
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class RegionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<RegionEntityDAO, RegionEntity>
) : SupportDatabaseDataSource<String, RegionEntityDAO, SaveRegionEntity, RegionEntity>(
    database,
    mapper,
    RegionEntityDAO
), IRegionDatabaseDataSource {

    override suspend fun save(data: SaveRegionEntity) {
        super.save(data)
        saveRegionCountries(data.toCountriesByRegion())
    }

    override suspend fun save(data: Iterable<SaveRegionEntity>) {
        super.save(data)
        saveRegionCountries(data.fold(listOf()) { items, region ->
            items + region.toCountriesByRegion()
        })
    }

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveRegionEntity) = with(entityToSave) {
        this@onMapEntityToSave[RegionTable.code] = code
        this@onMapEntityToSave[RegionTable.name] = name
    }

    private suspend fun saveRegionCountries(data: Iterable<Pair<String, String>>) {
        batchInsertOnDuplicateKeyUpdate(data) {
            this[RegionCountryTable.region] = it.first
            this[RegionCountryTable.region] = it.second
        }
    }
}