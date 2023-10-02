package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.EpgChannelProgrammeResponseDTO
import java.time.LocalDateTime

// Interface for EPG (Electronic Program Guide) channel program service
interface IEpgChannelProgrammeService {

    // Retrieve EPG programs for a specific channel within a date range
    @Throws(
        EpgChannelProgrammeServiceException.InternalServerError::class,
        EpgChannelProgrammeServiceException.EpgChannelNotFoundException::class
    )
    suspend fun findByChannelIdAndDateRange(
        channelId: String,   // ID of the channel for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): Iterable<EpgChannelProgrammeResponseDTO>

    // Retrieve EPG programs for multiple channels in a country within a date range
    @Throws(EpgChannelProgrammeServiceException.InternalServerError::class)
    suspend fun findByCountryAndDate(
        countryCode: String,   // Country code for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): Map<String, List<EpgChannelProgrammeResponseDTO>>
}

// Custom exceptions for the EPG channel program service
sealed class EpgChannelProgrammeServiceException(message: String) : Exception(message) {

    // Exception for when an EPG channel is not found
    class EpgChannelNotFoundException(code: String) : EpgChannelProgrammeServiceException("EPG for channel with code '$code' not found.")

    // Exception for internal server errors
    class InternalServerError(message: String) : EpgChannelProgrammeServiceException("Internal server error: $message")
}