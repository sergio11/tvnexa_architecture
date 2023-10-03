package com.dreamsoftware.data.database.datasource.channel

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity

/**
 * Interface for a data source that provides access to channel-related database operations.
 *
 * This interface extends the [ISupportDatabaseDataSource] interface, specifying the key type as [String],
 * the entity type as [ChannelEntity], and the save entity type as [SaveChannelEntity].
 */
interface IChannelDatabaseDataSource : ISupportDatabaseDataSource<String, SaveChannelEntity, ChannelEntity> {

    /**
     * Retrieves a list of channels filtered by category and country.
     *
     * @param categoryId The category ID to filter by. Pass null to exclude this filter.
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [ChannelEntity] objects matching the provided filters.
     */
    suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<ChannelEntity>

    /**
     * Retrieves a list of channels based on the provided country ID.
     *
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [ChannelEntity] objects matching the provided country filter.
     */
    suspend fun findByCountry(countryId: String): Iterable<ChannelEntity>

    /**
     * Retrieves a list of channels that allow generating Catchup content.
     *
     * @return An iterable collection of [ChannelEntity] objects representing channels that permit Catchup content.
     */
    suspend fun findChannelsAllowingCatchup(): Iterable<ChannelEntity>
}