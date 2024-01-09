package com.dreamsoftware.data.database.datasource.stream

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveChannelStreamEntity
import com.dreamsoftware.data.database.entity.ChannelStreamEntity

interface IStreamDatabaseDataSource: ISupportDatabaseDataSource<String, SaveChannelStreamEntity, ChannelStreamEntity>