package com.dreamsoftware.data.database.datasource.profiles

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.*
import java.util.*

/**
 * Interface defining methods for profile-related database operations.
 *
 * Extends [ISupportDatabaseDataSource] to inherit basic CRUD operations for profiles.
 */
interface IProfileDatabaseDataSource : ISupportDatabaseDataSource<UUID, CreateProfileEntity, ProfileEntity> {

    /**
     * Retrieves the number of profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return The number of profiles associated with the specified user.
     */
    suspend fun countByUser(uuid: String): Long
}