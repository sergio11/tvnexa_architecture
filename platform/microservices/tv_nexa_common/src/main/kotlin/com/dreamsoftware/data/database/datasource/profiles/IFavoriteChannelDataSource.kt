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
}