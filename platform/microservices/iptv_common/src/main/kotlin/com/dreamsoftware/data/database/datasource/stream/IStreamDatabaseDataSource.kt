package com.dreamsoftware.data.database.datasource.stream

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.SaveStreamEntity
import com.dreamsoftware.data.database.entity.StreamEntity

interface IStreamDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveStreamEntity, StreamEntity>