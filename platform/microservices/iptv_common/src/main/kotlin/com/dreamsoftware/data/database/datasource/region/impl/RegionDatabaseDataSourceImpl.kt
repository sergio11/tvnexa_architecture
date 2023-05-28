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
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.UpdateBuilder

internal class RegionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: IMapper<RegionEntityDAO, RegionEntity>
) : SupportDatabaseDataSource<String, RegionEntityDAO, SaveRegionEntity, RegionEntity>(
    database,
    mapper,
    RegionEntityDAO
), IRegionDatabaseDataSource {

    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveRegionEntity) = with(entityToSave) {
        this@onMapEntityToSave[RegionTable.code] = code
        this@onMapEntityToSave[RegionTable.name] = name
    }

    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveRegionEntity>) {
        saveRegionCountries(data.fold(listOf()) { items, region ->
            items + region.toCountriesByRegion()
        })
    }

    override fun Transaction.onSaveTransactionFinished(data: SaveRegionEntity) {
        saveRegionCountries(data.toCountriesByRegion())
    }

    private fun saveRegionCountries(data: Iterable<Pair<String, String>>) {
        RegionCountryTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                RegionCountryTable.region,
                RegionCountryTable.country
            ),
            data = data,
            onSaveData = {
                this[RegionCountryTable.region] = it.first
                this[RegionCountryTable.country] = it.second
            }
        )
    }
}