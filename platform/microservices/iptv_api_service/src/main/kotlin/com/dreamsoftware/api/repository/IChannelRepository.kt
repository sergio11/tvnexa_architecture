package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.ChannelEntity

/**
 * Interface for accessing channel data from a repository.
 */
interface IChannelRepository {

    /**
     * Retrieve all channels available in the repository.
     *
     * @return An iterable collection of all channel entities.
     */
    suspend fun findAll(): List<ChannelEntity>

    /**
     * Retrieves a paginated list of all available channel entities.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @return List of ChannelEntity containing a paginated list of channel entities.
     */
    suspend fun findPaginated(offset: Long, limit: Long): List<ChannelEntity>

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    suspend fun findById(id: String): ChannelEntity?

    /**
     * Filter channels by category and country.
     *
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return An iterable collection of channel entities matching the specified category and country filters.
     */
    suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): List<ChannelEntity>

    /**
     * Filter channels by country.
     *
     * @param countryId The unique identifier of the country to filter by.
     * @return An iterable collection of channel entities matching the specified country filter.
     */
    suspend fun filterByCountry(countryId: String): List<ChannelEntity>
}