package com.dreamsoftware.api.repository

import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import java.time.LocalDateTime

interface IEpgChannelProgrammeRepository {
    suspend fun findByChannelIdAndDateRange(channelId: String, startAt: LocalDateTime, endAt: LocalDateTime): Iterable<EpgChannelProgrammeEntity>
}