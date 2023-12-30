package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of the [IChannelRepository] interface that provides access to channel data
 * from a database data source.
 *
 * @property channelDataSource The data source responsible for retrieving channel data.
 */
internal class ChannelRepositoryImpl(
    private val channelDataSource: IChannelDatabaseDataSource
) : IChannelRepository {

    /**
     * Retrieve all channels available in the repository.
     *
     * @return An iterable collection of all channel entities.
     */
    override suspend fun findAll(): List<ChannelEntity> = withContext(Dispatchers.IO) {
        channelDataSource.findAll().toList()
    }

    /**
     * Retrieves a paginated list of all available channel entities.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @return List of ChannelEntity containing a paginated list of channel entities.
     */
    override suspend fun findPaginated(offset: Long, limit: Long): List<ChannelEntity>  = withContext(Dispatchers.IO) {
        channelDataSource.findPaginated(offset, limit.toInt()).toList()
    }

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    override suspend fun findById(id: String): ChannelEntity? = withContext(Dispatchers.IO) {
        channelDataSource.findByKey(id)
    }

    /**
     * Filter channels by category and country.
     *
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return An iterable collection of channel entities matching the specified category and country filters.
     */
    override suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): List<ChannelEntity> = withContext(Dispatchers.IO) {
        channelDataSource.filterByCategoryAndCountry(categoryId, countryId).toList()
    }

    /**
     * Filter channels by country.
     *
     * @param countryId The unique identifier of the country to filter by.
     * @return An iterable collection of channel entities matching the specified country filter.
     */
    override suspend fun filterByCountry(countryId: String): List<ChannelEntity> = withContext(Dispatchers.IO) {
        channelDataSource.findByCountry(countryId).toList()
    }
}