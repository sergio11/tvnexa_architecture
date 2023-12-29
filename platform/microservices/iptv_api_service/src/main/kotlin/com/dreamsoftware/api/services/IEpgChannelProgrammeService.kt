package com.dreamsoftware.api.services

import com.dreamsoftware.api.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.EpgChannelProgrammeResponseDTO
import java.time.LocalDateTime

// Interface for EPG (Electronic Program Guide) channel program service
interface IEpgChannelProgrammeService {

    // Retrieve EPG programs for a specific channel within a date range
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.EpgChannelNotFoundException::class
    )
    suspend fun findByChannelIdAndDateRange(
        channelId: String,   // ID of the channel for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): Iterable<EpgChannelProgrammeResponseDTO>

    // Retrieve EPG programs for multiple channels in a country within a date range
    @Throws(AppException.InternalServerError::class)
    suspend fun findByCountryAndDate(
        countryCode: String,   // Country code for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): Map<String, List<EpgChannelProgrammeResponseDTO>>
}
