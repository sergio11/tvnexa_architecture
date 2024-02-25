package com.dreamsoftware.data.database.datasource.profiles

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.*
import java.util.UUID

/**
 * Interface defining methods for managing favorite channel data in the database.
 */
interface IFavoriteChannelDataSource : ISupportDatabaseDataSource<Long, SaveFavoriteChannel, SimpleChannelEntity> {

    /**
     * Suspended function to retrieve a list of favorite channels for the specified profile.
     *
     * @param profile The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the favorite channels for the specified profile.
     */
    suspend fun findByProfile(profile: UUID): List<SimpleChannelEntity>

    /**
     * Deletes data associated with a specific profile and channel.
     * @param profileId The UUID of the profile associated.
     * @param channelId The ID of the channel
     */
    suspend fun deleteByProfileAndChannel(profileId: UUID, channelId: String)

    /**
     * Checks if a channel is saved as a favorite for a user profile.
     *
     * @param profileId The UUID of the user profile.
     * @param channelId The ID of the channel to be checked.
     * @return `true` if the channel is saved as a favorite for the user profile, `false` otherwise.
     */
    suspend fun isChannelSavedAsFavorite(profileId: UUID, channelId: String): Boolean
}