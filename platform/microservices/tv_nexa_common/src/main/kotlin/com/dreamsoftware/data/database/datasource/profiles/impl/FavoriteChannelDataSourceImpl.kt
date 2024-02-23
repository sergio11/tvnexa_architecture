package com.dreamsoftware.data.database.datasource.profiles.impl

import com.dreamsoftware.core.ISimpleMapper
import com.dreamsoftware.data.database.core.IDatabaseFactory
import com.dreamsoftware.data.database.dao.FavoriteChannelEntityDAO
import com.dreamsoftware.data.database.dao.FavoriteChannelsTable
import com.dreamsoftware.data.database.datasource.core.SupportDatabaseDataSource
import com.dreamsoftware.data.database.datasource.profiles.IFavoriteChannelDataSource
import com.dreamsoftware.data.database.entity.SaveFavoriteChannel
import com.dreamsoftware.data.database.entity.SimpleChannelEntity
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import java.util.*

/**
 * Implementation of the [IFavoriteChannelDataSource] interface for managing favorite channel data in the database.
 *
 * This class extends [SupportDatabaseDataSource] and provides methods to interact with the 'favorite_channels' table.
 *
 * @property database The [IDatabaseFactory] instance responsible for database interactions.
 * @property mapper The [ISimpleMapper] used to map between [FavoriteChannelEntityDAO] and [SimpleChannelEntity].
 */
internal class FavoriteChannelDataSourceImpl(
    database: IDatabaseFactory,
    mapper: ISimpleMapper<FavoriteChannelEntityDAO, SimpleChannelEntity>
) : SupportDatabaseDataSource<Long, FavoriteChannelEntityDAO, SaveFavoriteChannel, SimpleChannelEntity>(
    database,
    mapper,
    FavoriteChannelEntityDAO
), IFavoriteChannelDataSource {

    /**
     * Extension function used for mapping [SaveFavoriteChannel] entity to the corresponding database update builder.
     *
     * @param entityToSave The [SaveFavoriteChannel] entity to be mapped.
     */
    override fun UpdateBuilder<Int>.onMapEntityToSave(entityToSave: SaveFavoriteChannel) = with(entityToSave) {
        this@onMapEntityToSave[FavoriteChannelsTable.channel] = channelId
        this@onMapEntityToSave[FavoriteChannelsTable.profile] = profileId
    }

    /**
     * Retrieves a list of favorite channels associated with the specified user profile.
     *
     * @param profile The unique identifier of the user profile.
     * @return A list of [SimpleChannelEntity] objects representing the favorite channels.
     */
    override suspend fun findByProfile(profile: UUID): List<SimpleChannelEntity> = execQuery {
        entityDAO.find { FavoriteChannelsTable.profile eq profile }.map(mapper::map)
    }

    /**
     * Deletes data associated with a specific profile and channel.
     * @param profileId The UUID of the profile associated.
     * @param channelId The ID of the channel
     */
    override suspend fun deleteByProfileAndChannel(profileId: UUID, channelId: String): Unit = execWrite {
        entityDAO.find { FavoriteChannelsTable.profile eq profileId and(FavoriteChannelsTable.channel eq channelId) }
            .forEach { it.delete() }
    }
}