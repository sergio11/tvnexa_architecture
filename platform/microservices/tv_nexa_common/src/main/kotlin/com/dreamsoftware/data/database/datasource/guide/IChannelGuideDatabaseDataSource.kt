package com.dreamsoftware.data.database.datasource.guide

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelGuideAggregateEntity
import com.dreamsoftware.data.database.entity.ChannelGuideEntity
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity

/**
 * Interface representing a data source responsible for accessing channel guide data in a database.
 * This data source provides methods to interact with channel guide entities in the database.
 */
interface IChannelGuideDatabaseDataSource : ISupportDatabaseDataSource<String, SaveChannelGuideEntity, ChannelGuideEntity> {

    /**
     * Retrieves channel guide entities associated with a specific channel ID.
     *
     * @param channelId The unique identifier of the channel.
     * @return An iterable collection of [ChannelGuideEntity] related to the specified channel ID.
     */
    suspend fun findByChannelId(channelId: String): Iterable<ChannelGuideEntity>

    /**
     * Retrieves channel guide entities associated with a specific language ID.
     *
     * @param languageId The unique identifier of the language.
     * @return An iterable collection of [ChannelGuideEntity] related to the specified language ID.
     */
    suspend fun findByLanguageId(languageId: String): Iterable<ChannelGuideEntity>

    /**
     * Checks the existence of a channel guide entity based on the provided language ID and site.
     *
     * @param languageId The unique identifier of the language.
     * @param site The specific site or source.
     * @return `true` if a channel guide entity exists for the given language ID and site, otherwise `false`.
     */
    suspend fun existsByLanguageIdAndSite(languageId: String, site: String): Boolean

    /**
     * Retrieves channel guide entities grouped by site and language.
     *
     * @return An iterable collection of [ChannelGuideAggregateEntity] representing grouped channel guide data.
     */
    suspend fun findGroupBySiteAndLanguage(): Iterable<ChannelGuideAggregateEntity>
}
