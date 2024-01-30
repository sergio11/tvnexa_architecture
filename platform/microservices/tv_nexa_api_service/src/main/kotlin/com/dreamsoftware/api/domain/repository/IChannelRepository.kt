package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.ChannelDetailEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Interface for accessing channel data from a repository.
 */
interface IChannelRepository {

    /**
     * Retrieves a paginated list of all available channel entities.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return List of ChannelEntity containing a paginated list of channel entities.
     */
    suspend fun findByCategoryAndCountryPaginated(categoryId: String?, countryId: String?, offset: Long, limit: Long): List<SimpleChannelEntity>

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    suspend fun findById(id: String): ChannelDetailEntity?

    /**
     *  Filter channels by country.
     *
     *  @param countryId The unique identifier of the country to filter by.
     *  @return An iterable collection of channel entities matching the specified country filter.
     */
    suspend fun filterByCountry(countryId: String): List<SimpleChannelEntity>

    /**
     * Searches for channels whose names contain the specified term in a case-insensitive manner.
     *
     * @param term The search term to match against channel names.
     * @return A list of [SimpleChannelEntity] representing the channels found.
     */
    suspend fun findByNameLike(term: String): List<SimpleChannelEntity>
}