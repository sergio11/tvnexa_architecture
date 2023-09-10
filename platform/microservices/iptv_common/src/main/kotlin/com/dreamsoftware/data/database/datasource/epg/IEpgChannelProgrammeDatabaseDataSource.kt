package com.dreamsoftware.data.database.datasource.epg

import com.dreamsoftware.data.database.datasource.core.ISupportDatabaseDataSource
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import com.dreamsoftware.data.database.entity.SaveEpgChannelProgrammeEntity
import java.time.LocalDateTime

interface IEpgChannelProgrammeDatabaseDataSource: ISupportDatabaseDataSource<Long, SaveEpgChannelProgrammeEntity, EpgChannelProgrammeEntity> {

    fun findByChannelIdAndDateRange(channelId: String, startAt: LocalDateTime, endAt: LocalDateTime): Iterable<EpgChannelProgrammeEntity>
}