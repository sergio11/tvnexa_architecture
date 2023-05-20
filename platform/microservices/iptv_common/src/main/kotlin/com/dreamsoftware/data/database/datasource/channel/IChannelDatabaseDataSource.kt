package com.dreamsoftware.data.database.datasource.channel

import com.dreamsoftware.data.database.dao.ChannelEntityDAO
import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.ChannelEntity

interface IChannelDatabaseDataSource: ISupportDatabaseDataSource<ChannelEntityDAO, String, ChannelEntity>