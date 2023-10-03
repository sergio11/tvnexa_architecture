package com.dreamsoftware.data.database.datasource.channel.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * Implementation of the [IChannelDatabaseDataSource] interface for managing channel-related database operations.
 *
 * @param database The database factory used for database interactions.
 * @param mapper The mapper used to convert database entities to domain entities.
 */
internal class ChannelDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<ChannelEntityDAO, ChannelEntity>
) : SupportDatabaseDataSource<String, ChannelEntityDAO, SaveChannelEntity, ChannelEntity>(
    database,
    mapper,
    ChannelEntityDAO
), IChannelDatabaseDataSource {

    /**
     * Retrieves a list of channels filtered by category and country.
     *
     * @param categoryId The category ID to filter by. Pass null to exclude this filter.
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [ChannelEntity] objects matching the provided filters.
     */
    override suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity> = execQuery {
        entityDAO.find {
            if (!categoryId.isNullOrBlank() && !countryId.isNullOrBlank()) {
                (ChannelCategoryTable.category eq categoryId) and (ChannelTable.country eq countryId)
            } else if (!categoryId.isNullOrBlank()) {
                ChannelCategoryTable.category eq categoryId
            } else if (!countryId.isNullOrBlank()) {
                ChannelTable.country eq countryId
            } else {
                ChannelTable.id.isNotNull()
            }
        }.map(mapper::map)
    }

    /**
     * Retrieves a list of channels that allow generating Catchup content.
     *
     * @return An iterable collection of [ChannelEntity] objects representing channels that permit Catchup content.
     */
    override suspend fun findChannelsAllowingCatchup(): Iterable<ChannelEntity> = execQuery {
        entityDAO.find {
            ChannelTable.catchupEnabled eq true
        }.map(mapper::map)
    }

    /**
     * Retrieves a list of channels based on the provided country ID.
     *
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [ChannelEntity] objects matching the provided country filter.
     */
    override suspend fun findByCountry(countryId: String): Iterable<ChannelEntity> = execQuery {
        entityDAO.find {
            if (countryId.isNotBlank()) {
                ChannelTable.country eq countryId
            } else {
                ChannelTable.id.isNotNull()
            }
        }.map(mapper::map)
    }

    /**
     * Maps attributes from [SaveChannelEntity] to the database update builder.
     *
     * @param entityToSave The [SaveChannelEntity] to map.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveChannelEntity) = with(entityToSave) {
        this@onMapEntityToSave[ChannelTable.channelId] = channelId
        this@onMapEntityToSave[ChannelTable.name] = name
        this@onMapEntityToSave[ChannelTable.network] = network
        this@onMapEntityToSave[ChannelTable.country] = country
        this@onMapEntityToSave[ChannelTable.subdivision] = subdivision
        this@onMapEntityToSave[ChannelTable.city] = city
        this@onMapEntityToSave[ChannelTable.isNsfw] = isNsfw
        this@onMapEntityToSave[ChannelTable.launched] = launched
        this@onMapEntityToSave[ChannelTable.closed] = closed
        this@onMapEntityToSave[ChannelTable.replacedBy] = replacedBy
        this@onMapEntityToSave[ChannelTable.website] = website
        this@onMapEntityToSave[ChannelTable.logo] = logo
    }

    override fun Transaction.onSaveTransactionFinished(data: Iterable<SaveChannelEntity>) {
        with(data) {
            saveCategories(fold(listOf()) { items, channel ->
                items + channel.toCategoriesByChannel()
            })
            saveLanguages(fold(listOf()) { items, channel ->
                items + channel.toLanguagesByChannel()
            })
            saveChannelAltNames(fold(listOf()) { items, channel ->
                items + channel.toAltNamesByChannel()
            })
            saveChannelOwners(fold(listOf()) { items, channel ->
                items + channel.toOwnersByChannel()
            })
            saveChannelBroadcastAreas(fold(listOf()) { items, channel ->
                items + channel.toBroadcastAreaByChannel()
            })
        }
    }

    override fun Transaction.onSaveTransactionFinished(data: SaveChannelEntity) {
        with(data) {
            saveCategories(toCategoriesByChannel())
            saveLanguages(toLanguagesByChannel())
            saveChannelAltNames(toAltNamesByChannel())
            saveChannelOwners(toOwnersByChannel())
            saveChannelBroadcastAreas(toBroadcastAreaByChannel())
        }
    }

    private fun saveCategories(data: Iterable<Pair<String, String>>) {
        ChannelCategoryTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelCategoryTable.category,
                ChannelCategoryTable.channel
            ),
            data = data,
            onSaveData = {
                this[ChannelCategoryTable.channel] = it.first
                this[ChannelCategoryTable.category] = it.second
            }
        )
    }

    private fun saveLanguages(data: Iterable<Pair<String, String>>) {
        ChannelLanguageTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelLanguageTable.channel,
                ChannelLanguageTable.language
            ),
            data = data,
            onSaveData = {
                this[ChannelLanguageTable.channel] = it.first
                this[ChannelLanguageTable.language] = it.second
            }
        )
    }

    private fun saveChannelAltNames(data: Iterable<Pair<String, String>>) {
        ChannelNameTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelNameTable.channel,
                ChannelNameTable.altName
            ),
            data = data,
            onSaveData = {
                this[ChannelNameTable.channel] = it.first
                this[ChannelNameTable.altName] = it.second
            }
        )
    }

    private fun saveChannelOwners(data: Iterable<Pair<String, String>>) {
        ChannelOwnerTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelOwnerTable.channel,
                ChannelOwnerTable.owner
            ),
            data = data,
            onSaveData = {
                this[ChannelOwnerTable.channel] = it.first
                this[ChannelOwnerTable.owner] = it.second
            }
        )
    }

    private fun saveChannelBroadcastAreas(data: Iterable<Pair<String, String>>) {
        ChannelBroadcastAreaTable.batchInsertOnDuplicateKeyUpdate(
            onDupUpdateColumns = listOf(
                ChannelBroadcastAreaTable.channel,
                ChannelBroadcastAreaTable.broadcastArea
            ),
            data = data,
            onSaveData = {
                this[ChannelBroadcastAreaTable.channel] = it.first
                this[ChannelBroadcastAreaTable.broadcastArea] = it.second
            }
        )
    }
}