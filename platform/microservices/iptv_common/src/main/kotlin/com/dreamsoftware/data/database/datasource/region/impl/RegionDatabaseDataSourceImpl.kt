package com.dreamsoftware.data.database.datasource.region.impl

import com.dreamsoftware.core.ISimpleMapper
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

/**
 * Implementation of the [IRegionDatabaseDataSource] interface for managing region data in a database.
 *
 * @param database The [IDatabaseFactory] instance for accessing the database.
 * @param mapper The mapper to convert between the database entity ([RegionEntityDAO]) and domain entity ([RegionEntity]).
 */
internal class RegionDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<RegionEntityDAO, RegionEntity>
) : SupportDatabaseDataSource<String, RegionEntityDAO, SaveRegionEntity, RegionEntity>(
    database,
    mapper,
    RegionEntityDAO
), IRegionDatabaseDataSource {

    /**
     * Maps a [SaveRegionEntity] to a database update builder for saving to the database.
     *
     * @param entityToSave The [SaveRegionEntity] to be mapped.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveRegionEntity) = with(entityToSave) {
        this@onMapEntityToSave[RegionTable.code] = code
        this@onMapEntityToSave[RegionTable.name] = name
    }

    /**
     * Handles additional tasks after a transaction is finished for saving multiple region entities.
     *
     * @param data The collection of [SaveRegionEntity] instances that were saved.
     */
    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveRegionEntity>) {
        saveRegionCountries(data.fold(listOf()) { items, region ->
            items + region.toCountriesByRegion()
        })
    }

    /**
     * Handles additional tasks after a transaction is finished for saving a single region entity.
     *
     * @param data The [SaveRegionEntity] instance that was saved.
     */
    override fun Transaction.onSaveTransactionFinished(data: SaveRegionEntity) {
        saveRegionCountries(data.toCountriesByRegion())
    }

    /**
     * Saves the relationships between regions and countries in the database.
     *
     * @param data The collection of pairs representing region-country relationships.
     */
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