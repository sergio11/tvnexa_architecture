package com.dreamsoftware.data.database.datasource.profiles

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.*
import java.util.*

/**
 * Interface defining methods for managing blocked channel data in the database.
 */
interface IBlockedChannelDataSource : ISupportDatabaseDataSource<Long, SaveBlockedChannel, SimpleChannelEntity> {

    /**
     * Suspended function to retrieve a list of blocked channels for the specified profile.
     *
     * @param profile The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the blocked channels for the specified profile.
     */
    suspend fun findByProfile(profile: UUID): List<SimpleChannelEntity>

    /**
     * Deletes data associated with a specific profile and channel.
     * @param profileId The UUID of the profile associated.
     * @param channelId The ID of the channel
     */
    suspend fun deleteByProfileAndChannel(profileId: UUID, channelId: String)
}