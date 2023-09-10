package com.dreamsoftware.api.repository.impl

import com.dreamsoftware.api.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class EpgChannelProgrammeRepositoryImpl(
    private val epgChannelProgrammeDatabaseDataSource: IEpgChannelProgrammeDatabaseDataSource
): IEpgChannelProgrammeRepository {
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): Iterable<EpgChannelProgrammeEntity> = withContext(Dispatchers.IO) {
        epgChannelProgrammeDatabaseDataSource.findByChannelIdAndDateRange(channelId, startAt, endAt)
    }
}