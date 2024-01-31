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

    /**
     * Retrieves the profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    suspend fun findByUser(uuid: String): List<ProfileEntity>

    /**
     * Suspended function to update a user's profile information.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param data The [UpdateProfileEntity] containing the updated profile information.
     */
    suspend fun updateProfile(userUuid: String, profileUuid: String, data: UpdateProfileEntity)
}