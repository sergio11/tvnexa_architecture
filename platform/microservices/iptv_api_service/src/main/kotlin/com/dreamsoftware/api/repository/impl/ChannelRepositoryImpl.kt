package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IChannelRepository
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity

/**
 * Implementation of the [IChannelRepository] interface that provides access to channel data
 * from a database data source.
 *
 * @property channelDataSource The data source responsible for retrieving channel data.
 */
class ChannelRepositoryImpl(
    private val channelDataSource: IChannelDatabaseDataSource
) : IChannelRepository {

    /**
     * Retrieve all channels available in the repository.
     *
     * @return An iterable collection of all channel entities.
     */
    override suspend fun findAll(): Iterable<ChannelEntity> =
        channelDataSource.findAll()

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    override suspend fun findById(id: String): ChannelEntity =
        channelDataSource.findByKey(id)

    /**
     * Filter channels by category and country.
     *
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return An iterable collection of channel entities matching the specified category and country filters.
     */
    override suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity> =
        channelDataSource.filterByCategoryAndCountry(categoryId, countryId)

    /**
     * Filter channels by country.
     *
     * @param countryId The unique identifier of the country to filter by.
     * @return An iterable collection of channel entities matching the specified country filter.
     */
    override suspend fun filterByCountry(countryId: String): Iterable<ChannelEntity> =
        channelDataSource.findByCountry(countryId)
}