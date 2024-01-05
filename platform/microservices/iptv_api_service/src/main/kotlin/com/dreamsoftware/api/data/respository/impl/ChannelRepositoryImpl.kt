package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.data.cache.datasource.ICacheDatasource
import com.dreamsoftware.api.domain.repository.IChannelRepository
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
    private val channelDataSource: IChannelDatabaseDataSource,
    private val cacheDatasource: ICacheDatasource<String>
) : IChannelRepository {


    /**
     * Retrieves a paginated list of all available channel entities.
     *
     * @param offset The offset indicating the starting point from where channels should be fetched.
     * @param limit The maximum number of channel entities to be retrieved in a single request.
     * @param categoryId The unique identifier of the category to filter by (null for all categories).
     * @param countryId The unique identifier of the country to filter by (null for all countries).
     * @return List of ChannelEntity containing a paginated list of channel entities.
     */
    override suspend fun findByCategoryAndCountryPaginated(categoryId: String?, countryId: String?, offset: Long, limit: Long): List<ChannelEntity>  = withContext(Dispatchers.IO) {
        channelDataSource.findPaginated(offset, limit.toInt()).toList()
    }

    /**
     * Retrieve a channel by its unique identifier.
     *
     * @param id The unique identifier of the channel to retrieve.
     * @return The channel entity matching the specified ID.
     */
    override suspend fun findById(id: String): ChannelEntity? = withContext(Dispatchers.IO) {
        runCatching {
            cacheDatasource.find(id, ChannelEntity::class.java)
        }.getOrElse {
            channelDataSource.findByKey(id).also {
                runCatching {
                    cacheDatasource.save(id, it)
                }
            }
        }
    }

    /**
     *  Filter channels by country.
     *
     *  @param countryId The unique identifier of the country to filter by.
     *  @return An iterable collection of channel entities matching the specified country filter.
     */
    override suspend fun filterByCountry(countryId: String): List<ChannelEntity> = withContext(Dispatchers.IO) {
        channelDataSource.findByCountry(countryId).toList()
    }
}