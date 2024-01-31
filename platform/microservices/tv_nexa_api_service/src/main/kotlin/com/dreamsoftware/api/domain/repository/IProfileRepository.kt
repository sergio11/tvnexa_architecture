package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.ProfileEntity
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
    suspend fun countByUser(uuid: String): Long

    /**
     * Retrieves the profiles associated with a user.
     *
     * @param uuid The unique identifier of the user.
     * @return A list of [ProfileEntity] objects representing the user's profiles.
     */
    suspend fun findByUser(uuid: String): List<ProfileEntity>

    /**
     * Updates a user's profile information.
     *
     * @param userUuid The unique identifier of the user.
     * @param profileUuid The unique identifier of the profile to be updated.
     * @param profile The updated information for the user's profile.
     */
    suspend fun update(userUuid: String, profileUuid: String, profile: UpdateProfileEntity)

    /**
     * Retrieves a user's profile by its unique identifier.
     *
     * @param uuid The unique identifier of the profile.
     * @return A [ProfileEntity] object representing the user's profile, or null if not found.
     */
    suspend fun getProfileById(uuid: UUID): ProfileEntity?
}