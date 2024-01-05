package com.dreamsoftware.api.data.respository.impl

import com.dreamsoftware.api.domain.repository.IEpgChannelProgrammeRepository
import com.dreamsoftware.data.database.datasource.epg.IEpgChannelProgrammeDatabaseDataSource
import com.dreamsoftware.data.database.entity.EpgChannelProgrammeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

/**
 * Implementation of the [IEpgChannelProgrammeRepository] interface that provides access to
 * Electronic Program Guide (EPG) channel program data from a database data source.
 *
 * @property epgChannelProgrammeDatabaseDataSource The data source responsible for retrieving EPG data.
 */
internal class EpgChannelProgrammeRepositoryImpl(
    private val epgChannelProgrammeDatabaseDataSource: IEpgChannelProgrammeDatabaseDataSource
) : IEpgChannelProgrammeRepository {

    /**
     * Retrieve EPG programs for a specific channel within a date range.
     *
     * @param channelId The unique identifier of the channel for which to retrieve programs.
     * @param startAt The start date and time of the date range for program retrieval.
     * @param endAt The end date and time of the date range for program retrieval.
     * @return An iterable collection of EPG channel program entities matching the specified criteria.
     */
    override suspend fun findByChannelIdAndDateRange(
        channelId: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime
    ): List<EpgChannelProgrammeEntity> = withContext(Dispatchers.IO) {
        // Delegate the retrieval of EPG data to the database data source
        epgChannelProgrammeDatabaseDataSource.findByChannelIdAndDateRange(channelId, startAt, endAt).toList()
    }
}