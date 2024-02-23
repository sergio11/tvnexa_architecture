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

    /**
     * Checks whether a profile with the specified ID exists.
     * @param profileId The UUID of the profile to check for existence.
     * @return true if a profile with the specified ID exists, false otherwise.
     */
    suspend fun existsById(profileId: UUID): Boolean

    /**
     * Checks whether a profile with the specified ID can be managed by the user with the provided ID.
     * @param profileId The UUID of the profile to check for manageability.
     * @param userId The UUID of the user attempting to manage the profile.
     * @return true if the user can manage the profile, false otherwise.
     */
    suspend fun canBeManagedByUser(profileId: UUID, userId: UUID): Boolean

    /**
     * Checks whether the user has reached the limit of allowed profiles.
     * @param uuid The UUID of the user to check.
     * @return true if the user has reached the limit of allowed profiles, false otherwise.
     */
    suspend fun hasUserProfileLimitReached(uuid: UUID): Boolean
}