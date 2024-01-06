package com.dreamsoftware.data.database.datasource.channel

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelDetailEntity
import com.dreamsoftware.data.database.entity.SaveChannelEntity
import com.dreamsoftware.data.database.entity.SimpleChannelEntity

/**
 * Interface for a data source that provides access to channel-related database operations.
 *
 * This interface extends the [ISupportDatabaseDataSource] interface, specifying the key type as [String],
 * the entity type as [SimpleChannelEntity], and the save entity type as [SaveChannelEntity].
 */
interface IChannelDatabaseDataSource : ISupportDatabaseDataSource<String, SaveChannelEntity, SimpleChannelEntity> {

    /**
     * Retrieves a list of channels filtered by category and country.
     *
     * @param categoryId The category ID to filter by. Pass null to exclude this filter.
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [SimpleChannelEntity] objects matching the provided filters.
     */
    suspend fun filterByCategoryAndCountry(categoryId: String?, countryId: String?): Iterable<SimpleChannelEntity>

    /**
     * Retrieves a list of channels based on the provided country ID.
     *
     * @param countryId The country ID to filter by. Pass null to exclude this filter.
     * @return An iterable collection of [SimpleChannelEntity] objects matching the provided country filter.
     */
    suspend fun findByCountry(countryId: String): Iterable<SimpleChannelEntity>

    /**
     * Suspended function to find a specific ChannelDetailEntity by its [key].
     *
     * @param key The unique identifier used to find the ChannelDetailEntity.
     * @return The found ChannelDetailEntity if available, or null if not found.
     */
    suspend fun findDetailByKey(key: String): ChannelDetailEntity?
}