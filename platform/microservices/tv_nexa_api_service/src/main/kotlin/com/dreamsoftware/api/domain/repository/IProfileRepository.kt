package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.ProfileEntity


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
}