package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.data.respository.impl.core.SupportRepository
import com.dreamsoftware.api.domain.repository.IChannelRepository
import com.dreamsoftware.data.database.datasource.channel.IChannelDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelDetailEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Implementation of the [IChannelRepository] interface that provides access to channel data
 * from a database data source.
 *
 * @property channelDataSource The data source responsible for retrieving channel data.
 * @property cacheDatasource The cache data source used for caching channel data.
 */
internal class ChannelRepositoryImpl(
    private val channelDataSource: IChannelDatabaseDataSource,
    cacheDatasource: ICacheDatasource<String>
) : SupportRepository(cacheDatasource), IChannelRepository {

    private companion object {
        const val CHANNEL_DETAIL_CACHE_KEY_PREFIX = "channel:detail:"
        const val CHANNELS_BY_COUNTRY_CACHE_KEY_PREFIX = "channel:country:"
    }

    /**
     * Retrieves a paginated list of all available channel entities.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return List of ChannelEntity containing a paginated list of channel entities.
     */
    override suspend fun findByCategoryAndCountryPaginated(categoryId: String?, countryId: String?, offset: Long, limit: Long): List<SimpleChannelEntity>  =
        retrieveFromCacheOrElse(cacheKey = "${offset}_${limit}_${categoryId ?: "no_category"}_${countryId ?: "no_country"}") {
            channelDataSource.findPaginated(offset, limit.toInt()).toList()
        }

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    override suspend fun findById(id: String): ChannelDetailEntity? =
        retrieveFromCacheOrElse(cacheKey = CHANNEL_DETAIL_CACHE_KEY_PREFIX + id) {
            channelDataSource.findDetailByKey(id)
        }


    /**
     *  Filter channels by country.
     *
     *  @param countryId The unique identifier of the country to filter by.
     *  @return An iterable collection of channel entities matching the specified country filter.
     */
    override suspend fun filterByCountry(countryId: String): List<SimpleChannelEntity> =
        retrieveFromCacheOrElse(cacheKey = CHANNELS_BY_COUNTRY_CACHE_KEY_PREFIX + countryId) {
            channelDataSource.findByCountry(countryId).toList()
        }
}