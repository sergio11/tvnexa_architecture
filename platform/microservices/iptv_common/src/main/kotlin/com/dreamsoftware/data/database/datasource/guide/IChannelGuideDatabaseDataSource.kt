package com.dreamsoftware.data.database.datasource.guide

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelGuideEntity
import com.dreamsoftware.data.database.entity.SaveChannelGuideEntity

interface IChannelGuideDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveChannelGuideEntity, ChannelGuideEntity>