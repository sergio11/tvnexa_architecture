package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.CreateProfileEntity
import com.dreamsoftware.data.database.entity.ProfileEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity
import com.dreamsoftware.data.database.entity.UpdateProfileEntity
import java.util.*

/**
 * Interface defining methods for profile-related repository operations.
 */
interface IProfileRepository {

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
     * Updates a user's profile information.
     *
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param profile The updated information for the user's profile.
     */
    suspend fun update(profileUuid: UUID, profile: UpdateProfileEntity)

    /**
     * Retrieves a user's profile by its unique identifier.
     *
     * @param uuid The unique identifier of the profile.
     * @return A [ProfileEntity] object representing the user's profile, or null if not found.
     */
    suspend fun getProfileById(uuid: UUID): ProfileEntity?

    /**
     * Deletes a user's profile.
     *
     * @param uuid The unique identifier of the profile to be deleted.
     */
    suspend fun deleteProfile(uuid: UUID)

    /**
     * Creates a new profile for the authenticated user.
     *
     * @param data The [CreateProfileEntity] containing the data for the new profile.
     */
    suspend fun createProfile(data: CreateProfileEntity)

    /**
     * Verifies the PIN of a user's profile.
     *
     * @param profileUuid The unique identifier of the profile to be verified.
     * @param pin The PIN to be verified.
     * @return `true` if the PIN is verified successfully, `false` otherwise.
     */
    suspend fun verifyPin(profileUuid: UUID, pin: Int): Boolean

    /**
     * Suspended function to retrieve a list of blocked channels for the specified profile.
     *
     * @param profileUUID The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the blocked channels for the specified profile.
     */
    suspend fun getBlockedChannels(profileUUID: UUID): List<SimpleChannelEntity>

    /**
     * Suspended function to retrieve a list of favorite channels for the specified profile.
     *
     * @param profileUUID The unique identifier of the profile.
     * @return A list of [SimpleChannelEntity] representing the favorite channels for the specified profile.
     */
    suspend fun getFavoriteChannels(profileUUID: UUID): List<SimpleChannelEntity>

    /**
     * Saves a favorite channel for a given profile.
     * @param profileUUID The UUID of the profile for which the favorite channel will be saved.
     * @param channelId The ID of the channel to be saved as a favorite.
     * @return Unit
     * @throws Exception If an error occurs while saving the favorite channel.
     */
    suspend fun saveFavoriteChannel(profileUUID: UUID, channelId: String)

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
}