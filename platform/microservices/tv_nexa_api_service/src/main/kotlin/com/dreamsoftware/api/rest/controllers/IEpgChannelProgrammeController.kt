package com.dreamsoftware.api.rest.controllers

import com.dreamsoftware.api.domain.model.exceptions.AppException
import com.dreamsoftware.api.rest.dto.response.EpgChannelProgrammeResponseDTO
import java.time.LocalDateTime

// Interface for EPG (Electronic Program Guide) channel program service
interface IEpgChannelProgrammeController {

    // Retrieve EPG programs for a specific channel within a date range
    @Throws(
        AppException.InternalServerError::class,
        AppException.NotFoundException.EpgChannelNotFoundException::class
    )
    suspend fun findByChannelIdAndDateRange(
        channelId: String,   // ID of the channel for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): List<EpgChannelProgrammeResponseDTO>

    // Retrieve EPG programs for multiple channels in a country within a date range
    @Throws(AppException.InternalServerError::class)
    suspend fun findByCountryAndDate(
        countryCode: String,   // Country code for which to retrieve programs
        startAt: LocalDateTime,   // Start date and time of the date range
        endAt: LocalDateTime   // End date and time of the date range
    ): Map<String, List<EpgChannelProgrammeResponseDTO>>
}
