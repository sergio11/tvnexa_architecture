package com.dreamsoftware.data.database.datasource.channel.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.*
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelDetailEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import kotlin.reflect.KProperty1

/**
 * Implementation of the [IChannelDatabaseDataSource] interface for managing channel-related database operations.
 *
 * @param database The database factory used for database interactions.
 * @param simpleMapper The mapper used to convert database entities to domain entities.
 * @param detailMapper The mapper used to convert database entities to domain entities.
 */
internal class ChannelDatabaseDataSourceImpl(
    database: IDatabaseFactory,
    simpleMapper: ISimpleMapper<ChannelEntityDAO, SimpleChannelEntity>,
    private val detailMapper: ISimpleMapper<ChannelEntityDAO, ChannelDetailEntity>
) : SupportDatabaseDataSource<String, ChannelEntityDAO, SaveChannelEntity, SimpleChannelEntity>(
    database,
    simpleMapper,
    ChannelEntityDAO
), IChannelDatabaseDataSource {

    override val disableFkValidationsOnBatchOperation: Boolean
        get() = true

    override val findAllEagerRelationships: List<KProperty1<ChannelEntityDAO, Any?>>
        get() = listOf(ChannelEntityDAO::streams)

    /**
     * Retrieves a list of channels filtered by category and country.
     *
     * @param categoryId The category ID to filter by. Pass null to exclude this filter.
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @return An iterable collection of [SimpleChannelEntity] objects matching the provided filters.
     */
    override suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?, offset: Long, limit: Long): Iterable<SimpleChannelEntity> = execQuery {
        ChannelTable.join(
            ChannelCategoryTable, JoinType.LEFT, additionalConstraint = { ChannelTable.id eq ChannelCategoryTable.channel }
        )
            .slice(ChannelTable.columns)
            .select {
                val categoryPredicate = if (!categoryId.isNullOrBlank()) {
                    ChannelCategoryTable.category eq categoryId
                } else {
                    Op.TRUE
                }
                val countryPredicate = if (!countryId.isNullOrBlank()) {
                    ChannelTable.country eq countryId
                } else {
                    Op.TRUE
                }
                categoryPredicate and countryPredicate
            }
            .limit(limit.toInt(), offset = offset)
            .map(entityDAO::wrapRow)
            .map(mapper::map)
    }

    /**
     * Retrieves a list of channels based on the provided country ID.
     *
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [ChannelDetailEntity] objects matching the provided country filter.
     */
    override suspend fun findByCountry(countryId: String): Iterable<SimpleChannelEntity> = execQuery {
        entityDAO.find {
            if (countryId.isNotBlank()) {
                ChannelTable.country eq countryId
            } else {
                ChannelTable.id.isNotNull()
            }
        }.map(mapper::map)
    }

    /**
     * Suspended function to find a specific ChannelDetailEntity by its [key].
     *
     * @param key The unique identifier used to find the ChannelDetailEntity.
     * @return The found ChannelDetailEntity if available, or null if not found.
     */
    override suspend fun findDetailByKey(key: String): ChannelDetailEntity? = execQuery {
        onBuildFindByKeyQuery(key, eagerRelationships = listOf(
            ChannelEntityDAO::languages,
            ChannelEntityDAO::categories,
            ChannelEntityDAO::subdivision,
            ChannelEntityDAO::country,
            ChannelEntityDAO::replacedBy,
            ChannelEntityDAO::altNames,
            ChannelEntityDAO::owners,
            ChannelEntityDAO::broadcastAreas,
            ChannelEntityDAO::streams,
            ChannelEntityDAO::guides
        ))?.let(detailMapper::map)
    }

    /**
     * Searches for channels whose names contain the specified term in a case-insensitive manner.
     *
     * @param term The search term to match against channel names.
     * @return An iterable of [SimpleChannelEntity] representing the channels found.
     */
    override suspend fun findByNameLike(term: String): Iterable<SimpleChannelEntity> = execQuery {
        entityDAO.find {
            if (term.isNotBlank()) {
                ChannelTable.name.lowerCase() like "%${term.lowercase()}%"
            } else {
                ChannelTable.id.isNotNull()
            }
        }.map(mapper::map)
    }

    /**
     * Checks whether a channel with the specified ID exists.
     * @param channelId The ID of the user to check for existence.
     * @return true if a channel with the specified ID exists, false otherwise.
     */
    override suspend fun existsById(channelId: String): Boolean = execQuery {
        entityDAO.find { ChannelTable.id eq channelId }.count() > 0
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
            log.debug("onSaveTransactionFinished data size ${count()}")
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
        log.debug("saveCategories CALLED!")
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