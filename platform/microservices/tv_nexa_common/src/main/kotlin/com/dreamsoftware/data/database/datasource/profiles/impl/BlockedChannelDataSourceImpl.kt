package com.dreamsoftware.data.database.datasource.profiles.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.BlockedChannelEntityDAO
import com.dreamsoftware.data.database.dao.BlockedChannelsTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.profiles.IBlockedChannelDataSource
import com.dreamsoftware.data.database.entity.SaveBlockedChannel
import com.dreamsoftware.data.database.entity.SimpleChannelEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

/**
 * Implementation of the [IBlockedChannelDataSource] interface for managing blocked channel data in the database.
 *
 * This class extends [SupportDatabaseDataSource] and provides methods to interact with the 'blocked_channels' table.
 *
 * @property database The [IDatabaseFactory] instance responsible for database interactions.
 * @property mapper The [ISimpleMapper] used to map between [BlockedChannelEntityDAO] and [SimpleChannelEntity].
 */
internal class BlockedChannelDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<BlockedChannelEntityDAO, SimpleChannelEntity>
) : SupportDatabaseDataSource<Long, BlockedChannelEntityDAO, SaveBlockedChannel, SimpleChannelEntity>(
    database,
    mapper,
    BlockedChannelEntityDAO
), IBlockedChannelDataSource {

    /**
     * Extension function used for mapping [SaveBlockedChannel] entity to the corresponding database update builder.
     *
     * @param entityToSave The [SaveBlockedChannel] entity to be mapped.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveBlockedChannel) = with(entityToSave) {
        this@onMapEntityToSave[BlockedChannelsTable.channel] = channelId
        this@onMapEntityToSave[BlockedChannelsTable.profile] = profileId
    }

    /**
     * Retrieves a list of blocked channels associated with the specified user profile.
     *
     * @param profile The unique identifier of the user profile.
     * @return A list of [SimpleChannelEntity] objects representing the blocked channels.
     */
    override suspend fun findByProfile(profile: UUID): List<SimpleChannelEntity> = execQuery {
        entityDAO.find { BlockedChannelsTable.profile eq profile }.map(mapper::map)
    }

    /**
     * Deletes data associated with a specific profile and channel.
     * @param profileId The UUID of the profile associated.
     * @param channelId The ID of the channel
     */
    override suspend fun deleteByProfileAndChannel(profileId: UUID, channelId: String): Unit = execWrite {
        entityDAO.find { BlockedChannelsTable.profile eq profileId and(BlockedChannelsTable.channel eq channelId) }
            .forEach { it.delete() }
    }
}