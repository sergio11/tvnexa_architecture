package com.dreamsoftware.data.database.datasource.guide

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelGuideAggregateEntity
import com.dreamsoftware.data.database.entity.ChannelGuideEntity
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity

interface IChannelGuideDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveChannelGuideEntity, ChannelGuideEntity> {

    suspend fun findByChannelId(channelId: String): Iterable<ChannelGuideEntity>

    suspend fun findByLanguageId(languageId: String): Iterable<ChannelGuideEntity>

    suspend fun findGroupBySiteAndLanguage(): Iterable<ChannelGuideAggregateEntity>

}