package com.dreamsoftware.api.services

import com.dreamsoftware.api.dto.EpgChannelProgrammeResponseDTO
import java.time.LocalDateTime

interface IEpgChannelProgrammeService {
    @Throws(EpgChannelProgrammeServiceException.InternalServerError::class, EpgChannelProgrammeServiceException.EpgChannelNotFoundException::class)
    suspend fun findByChannelIdAndDateRange(channelId: String, startAt: LocalDateTime, endAt: LocalDateTime): Iterable<EpgChannelProgrammeResponseDTO>
}

sealed class EpgChannelProgrammeServiceException(message: String) : Exception(message) {
    class EpgChannelNotFoundException(code: String) : EpgChannelProgrammeServiceException("EPG for channel with code '$code' not found.")
    class InternalServerError(message: String) : EpgChannelProgrammeServiceException("Internal server error: $message")
}