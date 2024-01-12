package com.dreamsoftware.api.domain.repository

import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import java.time.LocalDateTime

/**
 * Interface for accessing Electronic Program Guide (EPG) channel program data from a repository.
 */
interface IEpgChannelProgrammeRepository {

    /**
     * Retrieve EPG programs for a specific channel within a date range.
     *
     * @param channelId The unique identifier of the channel for which to retrieve programs.
     * @param startAt The start date and time of the date range for program retrieval.
     * @param endAt The end date and time of the date range for program retrieval.
     * @return An iterable collection of EPG channel program entities matching the specified criteria.
     */
    suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): List<EpgChannelProgrammeEntity>
}