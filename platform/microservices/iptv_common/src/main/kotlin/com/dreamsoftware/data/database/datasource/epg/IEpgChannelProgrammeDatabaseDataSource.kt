package com.dreamsoftware.data.database.datasource.epg

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity

interface IEpgChannelProgrammeDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveEpgChannelProgrammeEntity, EpgChannelProgrammeEntity> {

}