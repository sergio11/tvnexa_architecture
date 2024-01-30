package com.dreamsoftware.api.domain.repository


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
}