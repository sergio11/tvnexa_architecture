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
    suspend fun countByUser(uuid: UUID): Long

    /**
     * Retrieves the profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    suspend fun findByUser(uuid: UUID): List<ProfileEntity>

    /**
     * Suspended function to update a user's profile information.
     *
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param data The [UpdateProfileEntity] containing the updated profile information.
     */
    suspend fun updateProfile(profileUuid: UUID, data: UpdateProfileEntity)

    /**
     * Verifies the PIN of a user's profile.
     *
     * @param profileUuid The unique identifier of the profile to be verified.
     * @param pin The PIN to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     */
    suspend fun verifyPin(profileUuid: UUID, pin: Int): Boolean
}